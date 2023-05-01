package be.smals.yoga.controller;

import be.smals.yoga.entity.UserCard;
import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.model.CardStatus;
import be.smals.yoga.model.SlotBooking;
import be.smals.yoga.service.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import static be.smals.yoga.model.MailText.*;

@RestController
@RequestMapping("/private")
@RequiredArgsConstructor
public class PrivateCardApi {

    private final UserCardService userCardService;
    private final UserService userService;
    private final SlotService slotService;
    private final MailService mailService;

    @PostMapping("/cards")
    public UserCard create() throws MessagingException {
        final YogaUser user = userService.findByUserId(userId(), false);
        final var newCard = new UserCard();
        newCard.setStatus(CardStatus.PENDING);
        newCard.setOwner(user);
        // TODO change TO
        mailService.sendSimpleMessage("soldiertt@gmail.com", SUBJECT_USER_CARD_REQUEST, BODY_USER_CARD_REQUEST);
        mailService.sendSimpleMessage("soldiertt@gmail.com", SUBJECT_ADMIN_CARD_REQUEST, BODY_ADMIN_CARD_REQUEST);
        user.getCards().add(newCard);
        userService.save(user);
        return Sanitizer.forPrivateCard(userCardService.save(newCard));
    }

    @PostMapping("/cards/slots")
    public UserCard bookSlot(@RequestBody final SlotBooking slotBooking) throws MessagingException {
        Assert.notNull(slotBooking.getSlotId(), "SlotId is required in body");
        final YogaUser user = userService.findByUserId(userId(), false);

        final var firstBookableCard = user.getCards().stream()
                .filter(c -> CardStatus.ACTIVE.equals(c.getStatus()) && c.getSlots().size() < c.getCapacity())
                .findFirst();
        if (firstBookableCard.isEmpty()) {
            throw new IllegalArgumentException("Cannot find any valid card to book " + slotBooking);
        }
        final var slot = slotService.findById(slotBooking.getSlotId());
        final var card = firstBookableCard.get();
        card.getSlots().add(slot);
        mailService.sendSimpleMessage("soldiertt@gmail.com", SUBJECT_ADMIN_SLOT_BOOKING + slot.getCourseDate(),
                BODY_ADMIN_SLOT_BOOKING);
        if (Boolean.TRUE.equals(slotBooking.getEmailConfirmation())) {
            // TODO change TO
            mailService.sendSimpleMessage("soldiertt@gmail.com", SUBJECT_USER_SLOT_BOOKING,
                    String.format(BODY_USER_SLOT_BOOKING, slot.getCourseDate(), slot.getCourseTime()));
        }
        return Sanitizer.forPrivateCard(userCardService.save(card));
    }

    @DeleteMapping("/cards/{cardId}/slots/{slotId}")
    public UserCard cancelSlot(@PathVariable final Long cardId, @PathVariable final Long slotId) throws MessagingException {
        final var card = userCardService.findById(cardId);
        if (card == null || !card.getOwner().getUserId().equals(userId())) {
            throw new IllegalArgumentException("Cannot find any valid card with id " + cardId);
        }
        card.getSlots().removeIf(s -> s.getId().equals(slotId));
        mailService.sendSimpleMessage("soldiertt@gmail.com", SUBJECT_ADMIN_SLOT_CANCELLED, BODY_ADMIN_SLOT_CANCELLED);
        return Sanitizer.forPrivateCard(userCardService.save(card));
    }

    private static String userId() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
