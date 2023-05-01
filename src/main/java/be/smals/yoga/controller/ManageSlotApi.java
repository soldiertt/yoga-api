package be.smals.yoga.controller;

import be.smals.yoga.entity.Slot;
import be.smals.yoga.service.Sanitizer;
import be.smals.yoga.service.SlotService;
import be.smals.yoga.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manage/slots")
@RequiredArgsConstructor
public class ManageSlotApi {

    private final SlotService slotService;
    private final UserService userService;

    @GetMapping
    public List<Slot> findAll() {
        final var slots = slotService.findAll();
        slots.forEach(slot -> {
            if (slot.getCards() != null) {
                slot.getCards().forEach(card -> {
                    card.setOwner(userService.fillWithMetadata(card.getOwner()));
                });
            }
        });
        return Sanitizer.forManageSlots(slots);
    }

    @PostMapping
    public Slot save(@RequestBody final Slot slot) {
        final var newSlot = slotService.save(slot);
        newSlot.setParticipantsCount(0);
        return newSlot;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        slotService.delete(id);
    }
}
