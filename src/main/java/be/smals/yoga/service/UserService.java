package be.smals.yoga.service;

import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.repository.Auth0Client;
import be.smals.yoga.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Auth0Client auth0Client;

    public List<YogaUser> findAll() {
        return userRepository.findAll();
    }

    public YogaUser findByUserId(final String userId) {
        final var optUser = userRepository.findByUserId(userId);
        return optUser.orElseGet(() -> {
            final var auth0User = auth0Client.userInfo(userId);
            final var yogaUser = new YogaUser(userId, auth0User.getEmail());
            if (auth0User.getGiven_name() != null) {
                yogaUser.setFirstName(auth0User.getGiven_name());
            }
            if (auth0User.getFamily_name() != null) {
                yogaUser.setLastName(auth0User.getFamily_name());
            }
            if (auth0User.getPhone_number() != null) {
                yogaUser.setPhone(auth0User.getPhone_number());
            }
            return userRepository.save(yogaUser);
        });
    }

    public void update(final String userId, final YogaUser partialUser) {
        final var optUser = userRepository.findByUserId(userId);
        optUser.ifPresent(user -> {
            if (partialUser.getFirstName() != null) {
                user.setFirstName(partialUser.getFirstName());
            }
            if (partialUser.getLastName() != null) {
                user.setLastName(partialUser.getLastName());
            }
            if (partialUser.getPhone() != null) {
                user.setPhone(partialUser.getPhone());
            }
            userRepository.save(user);
        });
    }

    @Transactional
    public YogaUser save(final YogaUser user) {
        return userRepository.save(user);
    }
}
