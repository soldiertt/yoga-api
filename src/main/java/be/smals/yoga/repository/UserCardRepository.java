package be.smals.yoga.repository;

import be.smals.yoga.entity.UserCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {

  List<UserCard> findAllByOrderByCreatedTimeAsc();

  @Query("SELECT c FROM UserCard c LEFT JOIN FETCH c.slots ORDER BY c.createdTime")
  List<UserCard> findAllWithSlotsByOrderByCreatedTimeAsc();

}
