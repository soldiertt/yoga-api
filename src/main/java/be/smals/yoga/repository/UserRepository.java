package be.smals.yoga.repository;

import be.smals.yoga.entity.YogaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<YogaUser, Long> {

    Optional<YogaUser> findByUserId(String userId);
}
