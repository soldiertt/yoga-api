package be.smals.yoga.schedule;

import be.smals.yoga.service.UserCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobScheduler {

  private final UserCardService cardService;

  @Scheduled(cron = "0 0 0/12 * * ?")
  public void checkCardValidity() {
    log.info("Checking card validity");
    final var allCards = cardService.findAllWithSlots();
    cardService.checkForFullCardExpiration(allCards);
    cardService.checkForCardExpiration(allCards);
  }

}
