package be.smals.yoga.service;

import be.smals.yoga.entity.UserCard;
import be.smals.yoga.repository.UserCardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;
    private final UserService userService;

    @Transactional
    public UserCard save(final UserCard userCard) {
        return userCardRepository.save(userCard);
    }

    @Transactional
    public UserCard update(final UserCard partialUserCard) {
        return userCardRepository.findById(partialUserCard.getId()).map(userCard -> {
            userCard.setStatus(partialUserCard.getStatus());
            return userCard;
        }).orElse(null);
    }

    @Transactional
    public void delete(final Long id) {
        userCardRepository.deleteById(id);
    }

    public List<UserCard> findAll() {
        return userCardRepository.findAllByOrderByCreatedTimeAsc();
    }

    public UserCard findById(final Long id) {
        return userCardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find card with id " + id));
    }

}
