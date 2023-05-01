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
        return Sanitizer.forManageSlots(slotService.findAll());
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
