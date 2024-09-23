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
import static be.smals.yoga.model.Settings.ADMIN_EMAIL;

@RestController
@RequestMapping("/private")
@RequiredArgsConstructor
public class PrivateSlotApi {

    private final UserCardService userCardService;
    private final UserService userService;
    private final SlotService slotService;
    private final MailService mailService;

    @PostMapping("/cards/slots")
    public UserCard bookSlot(@RequestBody final SlotBooking slotBooking) throws MessagingException {
        Assert.notNull(slotBooking.getSlotId(), "SlotId is required in body");
        final YogaUser user = userService.findByUserId(userId());

        final var firstBookableCard = user.getCards().stream()
                .filter(c -> CardStatus.ACTIVE.equals(c.getStatus()) && c.getSlots().size() < c.getCapacity())
                .findFirst();
        if (firstBookableCard.isEmpty()) {
            throw new IllegalArgumentException("Cannot find any valid card to book " + slotBooking);
        }
        final var slot = slotService.findById(slotBooking.getSlotId());
        final var card = firstBookableCard.get();
        card.getSlots().add(slot);
        mailService.sendSimpleMessage(ADMIN_EMAIL, SUBJECT_ADMIN_SLOT_BOOKING + slot.getCourseTimestamp(),
                BODY_ADMIN_SLOT_BOOKING);
        if (Boolean.TRUE.equals(slotBooking.getEmailConfirmation())) {
            mailService.sendSimpleMessage(user.getEmail(), SUBJECT_USER_SLOT_BOOKING,
                    String.format(BODY_USER_SLOT_BOOKING, slot.getCourseTimestamp().toLocalDate(), slot.getCourseTime()));
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
        mailService.sendSimpleMessage(ADMIN_EMAIL, SUBJECT_ADMIN_SLOT_CANCELLED, BODY_ADMIN_SLOT_CANCELLED);
        return Sanitizer.forPrivateCard(userCardService.save(card));
    }

    private static String userId() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
