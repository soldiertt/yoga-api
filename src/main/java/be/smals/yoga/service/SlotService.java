package be.smals.yoga.service;

import be.smals.yoga.entity.Slot;
import be.smals.yoga.repository.SlotRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;

    @Transactional
    public Slot save(final Slot slot) {
        return slotRepository.save(slot);
    }

    @Transactional
    public void delete(final Long id) {
        slotRepository.deleteById(id);
    }

    public List<Slot> findAll() {
        return slotRepository.findAllByOrderByCourseDate();
    }

    public List<Slot> findAllFutureSlots() {
        return slotRepository.findAllFutureSlots();
    }

    public Slot findById(final Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find slot with id " + id));
    }
}
