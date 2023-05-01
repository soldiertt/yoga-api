package be.smals.yoga.controller;

import be.smals.yoga.entity.UserCard;
import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.model.CardStatus;
import be.smals.yoga.service.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static be.smals.yoga.model.MailText.*;

@RestController
@RequestMapping("/private/cards")
@RequiredArgsConstructor
public class PrivateCardApi {

    private final UserCardService userCardService;
    private final UserService userService;
    private final MailService mailService;

    @PostMapping
    public UserCard create() throws MessagingException {
        final YogaUser user = userService.findByUserId(userId());
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

    private static String userId() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
