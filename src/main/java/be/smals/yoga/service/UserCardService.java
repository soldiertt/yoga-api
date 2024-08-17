package be.smals.yoga.service;

import be.smals.yoga.entity.UserCard;
import be.smals.yoga.repository.UserCardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static be.smals.yoga.model.CardStatus.ACTIVE;
import static be.smals.yoga.model.CardStatus.EXPIRED;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;

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

    @Transactional
    public void checkForCardExpiration(final List<UserCard> cards) {
        final Predicate<UserCard> cardIsFull = card -> ACTIVE.equals(card.getStatus()) &&
                card.getSlots() != null && card.getCapacity().equals(card.getSlots().size());
        cards.stream().filter(cardIsFull).forEach(this::checkNeedExpiredStatus);
    }

    private void checkNeedExpiredStatus(final UserCard card) {
        if (card.getExpirationTime().isBefore(LocalDate.now().atStartOfDay()) || card.getSlots().stream().allMatch(slot -> slot.getCourseDate().isBefore(LocalDate.now()))) {
            card.setStatus(EXPIRED);
            userCardRepository.save(card);
        }
    }
}
