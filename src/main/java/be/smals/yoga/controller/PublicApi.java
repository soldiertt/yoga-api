package be.smals.yoga.controller;

import be.smals.yoga.entity.Slot;
import be.smals.yoga.service.Sanitizer;
import be.smals.yoga.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicApi {

    private final SlotService slotService;

    @GetMapping("/slots")
    public List<Slot> findAll() {
        final var allFutureSlots = slotService.findAllFutureSlots();
        return Sanitizer.forPublicSlots(allFutureSlots);
    }

}
