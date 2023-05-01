package be.smals.yoga.controller;

import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.service.Sanitizer;
import be.smals.yoga.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manage/users")
@RequiredArgsConstructor
public class ManageUserApi {

    private final UserService userService;

    @GetMapping
    public List<YogaUser> findAll() {
        return Sanitizer.forManageUsers(userService.findAll(true));
    }

}
