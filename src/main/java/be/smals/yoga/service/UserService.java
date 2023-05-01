package be.smals.yoga.service;

import be.smals.yoga.repository.Auth0Client;
import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Auth0Client auth0Api;

    public List<YogaUser> findAll(final boolean withMetadata) {
        final var users = userRepository.findAll();
        if (withMetadata) {
            users.forEach(this::fillWithMetadata);
        }
        return users;
    }

    public YogaUser findByUserId(final String userId, final boolean withMetadata) {
        final var optUser = userRepository.findByUserId(userId);
        return optUser.map(dbUser -> withMetadata ? fillWithMetadata(dbUser) : dbUser)
                .orElseGet(() -> userRepository.save(new YogaUser(userId)));
    }

    public YogaUser fillWithMetadata(final YogaUser user) {
        final var auth0User = auth0Api.userInfo(user.getUserId());
        user.setFirstName(auth0User.getUser_metadata().getFirst_name());
        user.setLastName(auth0User.getUser_metadata().getLast_name());
        user.setPhone(auth0User.getUser_metadata().getPhone());
        user.setEmail(auth0User.getEmail());
        return user;
    }

    public void updateMetadata(final String userId, final YogaUser user) {
        auth0Api.updateUser(userId, user);
    }
    @Transactional
    public YogaUser save(final YogaUser user) {
        return userRepository.save(user);
    }
}
