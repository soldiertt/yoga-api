package be.smals.yoga.repository;

import be.smals.yoga.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {

    List<Slot> findAllByOrderByCourseTimestamp();

    @Query("select s from Slot s where s.courseTimestamp >= now() order by s.courseTimestamp")
    List<Slot> findAllFutureSlots();
}
