package uj.pwkp.gr1.vet.VetApp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VisitRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.entity.Vet;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.repository.VetRepository;
import uj.pwkp.gr1.vet.VetApp.repository.VisitRepository;

@Slf4j
@Service
public class VisitService {

  @Autowired
  private VisitRepository visitRepository;

  @Autowired
  private VetRepository vetRepository;

  public List<Visit> getAllVisits() {
    return visitRepository.findAll();
  }

  public Either<VisitCreationResult, Visit> createVisit(VisitRequest req) {
    if (!dateAvailable(req.getStartTime(), req.getDuration())) {
      return Either.left(VisitCreationResult.OVERLAP);//OpResult.fail(VisitCreationResult.OVERLAP);
    }
    Visit v;
    try {
      Vet vet = vetRepository.findById(req.getVetId()).orElseGet(() -> {
        log.info(String.format("vet with this id: %x was not found", req.getVetId()));
        return Vet.builder().firstName("anonymous").lastName("anonymous")
            .id(req.getVetId())
            .build();
      });
      v = visitRepository.save(
          Visit.builder()
              .id(-1)
              .startTime(req.getStartTime())
              .duration(req.getDuration())
              .animalType(req.getAnimalType())
              .status(Status.PLANNED)
              .price(req.getPrice())
              .description(req.getDescription())
//                .vet(vet)
              .build());
    } catch (Exception e) {
      return Either.left(VisitCreationResult.REPOSITORY_PROBLEM);
    }
    return Either.right(v);

  }

  private boolean dateAvailable(LocalDateTime startTime, Duration duration) {
    List<Visit> overlaps = visitRepository
        .overlaps(startTime, startTime.plusMinutes(duration.toMinutes()));
    overlaps.forEach(x -> log.info(x.toString()));
    return overlaps.isEmpty();
  }

  public Optional<Visit> delete(@NotNull int id) {
    var visit = visitRepository.findById(id);
    return Optional.of(visit).map(v -> {
      visitRepository.deleteById(v.get().getId());
      return v;
    }).orElseGet(() -> {
      log.info(String.format("Visit with such id: %x was not found", id));
      return Optional.empty();
    });
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
