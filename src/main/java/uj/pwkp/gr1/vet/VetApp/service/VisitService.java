package uj.pwkp.gr1.vet.VetApp.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.VisitRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.repository.VisitRepository;

@Slf4j
@Service
public class VisitService {

  @Autowired
  private VisitRepository visitRepository;

  public List<Visit> getAllVisits() {
    return visitRepository.findAll();
  }

  public Either<VisitCreationResult, Visit> createVisit(VisitRequest req) {
    if (!dateAvailable(req.getStartTime(), req.getDuration())) {
      return Either.left(VisitCreationResult.OVERLAP);//OpResult.fail(VisitCreationResult.OVERLAP);
    } else {
      Visit v;
      try {
        v = visitRepository.save(
            Visit.builder()
                .id(-1)
                .startTime(req.getStartTime())
                .duration(req.getDuration())
                .animal(req.getAnimal())
                .status(Status.PLANNED)
                .price(req.getPrice())
                .build());
      } catch (Exception e) {
        return Either.left(VisitCreationResult.REPOSITORY_PROBLEM);
      }
      return Either.right(v);
    }
  }

  private boolean dateAvailable(LocalDateTime startTime, Duration duration) {
    List<Visit> overlaps = visitRepository
        .overlaps(startTime, startTime.plusMinutes(duration.toMinutes()));
    overlaps.forEach(System.out::println);
    return overlaps.size() == 0;
  }

  public Optional<Visit> delete(@NotNull int id) {
    var visit = visitRepository.findById(id);
    return Optional.ofNullable(visit).map(v -> {
      visitRepository.deleteById(v.get().getId());
      return v;
    }).orElseGet(Optional::empty);
  }

  public Optional<Visit> getVisitById(@NotNull int id) {
    return visitRepository.findById(id);
  }

  public Optional<Visit> updateVisitStatus(int id, Status status) {
    var visit = visitRepository.findById(id);
    if (visit.isPresent()) {
      try {
        log.info("Updating DB");
        visitRepository.updateStatus(id, status);
      } catch (Exception e) {
        System.out.println(e.fillInStackTrace());
        return Optional.empty();
      }
      log.info("DB update success");
      return visit;
    } else {
      return Optional.empty();
    }
  }
}
