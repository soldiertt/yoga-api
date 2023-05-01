package be.smals.yoga.controller;

import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.service.Sanitizer;
import be.smals.yoga.service.UserCardService;
import be.smals.yoga.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private/users")
@RequiredArgsConstructor
public class PrivateUserApi {

    private final UserService userService;
    private final UserCardService cardService;

    @GetMapping
    public YogaUser getUser() {
        final var user = userService.findByUserId(userId());
        if (user.getCards() != null) {
            cardService.checkForCardExpiration(user.getCards());
        }
        return Sanitizer.forPrivateUser(user);
    }

    @PatchMapping
    public void updateMetadata(@RequestBody final YogaUser user) {
        userService.update(userId(), user);
    }

    private static String userId() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
