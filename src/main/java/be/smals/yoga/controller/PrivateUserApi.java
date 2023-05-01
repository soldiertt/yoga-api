package be.smals.yoga.controller;

import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private")
@RequiredArgsConstructor
public class PrivateUserApi {

    private final UserService userService;

    @GetMapping("/users")
    public YogaUser getUser() {
        return Sanitizer.forPrivateUser(userService.findByUserId(userId()));
    }

    @PatchMapping("/users")
    public void updateMetadata(@RequestBody final YogaUser user) {
        userService.update(userId(), user);
    }

    private static String userId() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
