package be.smals.yoga.controller;

import be.smals.yoga.entity.UserCard;
import be.smals.yoga.service.Sanitizer;
import be.smals.yoga.service.UserCardService;
import be.smals.yoga.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static be.smals.yoga.model.MailText.BODY_USER_HAS_CARD_VALIDATED;
import static be.smals.yoga.model.MailText.SUBJECT_USER_HAS_CARD_VALIDATED;

@RestController
@RequestMapping("/manage/cards")
@RequiredArgsConstructor
public class ManageCardApi {

    private final UserCardService userCardService;
    private final MailService mailService;

    @GetMapping
    public Collection<UserCard> findAll() {
        return Sanitizer.forManageCards(userCardService.findAll());
    }

    @PatchMapping("/{id}")
    public UserCard update(@PathVariable final Long id, @RequestBody final UserCard userCard) throws MessagingException {
        if (id.equals(userCard.getId())) {
            final var card = userCardService.findById(id);
            mailService.sendSimpleMessage(card.getOwner().getEmail(), SUBJECT_USER_HAS_CARD_VALIDATED, BODY_USER_HAS_CARD_VALIDATED);
            return Sanitizer.forManageCard(userCardService.update(userCard));
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        userCardService.delete(id);
    }
}
