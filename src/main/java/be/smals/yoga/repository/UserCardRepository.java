package be.smals.yoga.repository;

import be.smals.yoga.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {

    List<UserCard> findAllByOrderByCreatedTimeAsc();
}
