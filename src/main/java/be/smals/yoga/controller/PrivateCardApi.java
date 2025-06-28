package be.smals.yoga.controller;

import be.smals.yoga.entity.UserCard;
import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.service.*;
import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static be.smals.yoga.model.MailText.*;
import static be.smals.yoga.model.Settings.ADMIN_EMAIL;

@RestController
@RequestMapping("/private/cards")
@RequiredArgsConstructor
public class PrivateCardApi {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  private final UserCardService userCardService;
  private final UserService userService;
  private final MailService mailService;


  @PostMapping("/long")
  public UserCard createLong() throws MessagingException {
    final var expirationTime = LocalDateTime.now().plusYears(1);
    return saveCard(160.0f, expirationTime);
  }

  @PostMapping("/short")
  public UserCard createShort() throws MessagingException {
    final var expirationTime = LocalDateTime.of(2025, 12, 13, 0, 0);
    return saveCard(140.0f, expirationTime);
  }

  private UserCard saveCard(final Float cardPrice, final LocalDateTime expirationTime) throws MessagingException {
    final YogaUser user = userService.findByUserId(userId());
    final var newCard = new UserCard(cardPrice, expirationTime, user);
    mailService.sendSimpleMessage(user.getEmail(), SUBJECT_USER_CARD_REQUEST,
        String.format(BODY_USER_CARD_REQUEST, cardPrice, expirationTime.format(formatter)));
    mailService.sendSimpleMessage(ADMIN_EMAIL, SUBJECT_ADMIN_CARD_REQUEST, BODY_ADMIN_CARD_REQUEST);
    user.getCards().add(newCard);
    userService.save(user);
    return Sanitizer.forPrivateCard(userCardService.save(newCard));
  }

  private static String userId() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
  }
}
