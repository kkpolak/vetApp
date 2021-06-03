package uj.pwkp.gr1.vet.VetApp.other;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.service.VisitService;

@Component
public class ScheduledTask {

  @Autowired
  VisitService visitService;

  @Scheduled(fixedRate = 3600000)
  public void updateVisitStatusEveryHour() {
    var visitList = visitService.getAllVisits();
    visitList.forEach(v -> {
      if (ChronoUnit.MINUTES.between(v.getStartTime(), LocalDateTime.now()) < 0) {
        visitService.updateVisitStatus(v.getId(), Status.FINISHED_AUTOMATICALLY);
      }
    });
  }

}
