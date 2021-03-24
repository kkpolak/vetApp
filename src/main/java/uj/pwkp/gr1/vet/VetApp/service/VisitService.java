package uj.pwkp.gr1.vet.VetApp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VisitRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.AnimalType;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.entity.Vet;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.repository.AnimalRepository;
import uj.pwkp.gr1.vet.VetApp.repository.ClientRepository;
import uj.pwkp.gr1.vet.VetApp.repository.VetRepository;
import uj.pwkp.gr1.vet.VetApp.repository.VisitRepository;

@Slf4j
@Service
public class VisitService {

  @Autowired
  private VisitRepository visitRepository;

  @Autowired
  private VetRepository vetRepository;

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private ClientRepository clientRepository;

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
      Animal animal = animalRepository.findById(req.getAnimalId()).orElseGet(() -> {
        log.info(String.format("animal with this id: %x was not found", req.getAnimalId()));
        return Animal.builder().name("anonymous").id(req.getAnimalId()).build();
      });
      Client client = clientRepository.findById(req.getClientId()).orElseGet(() -> {
        log.info(String.format("client with this id: %x was not found", req.getClientId()));
        return Client.builder().firstName("anonymous").lastName("anonymous").id(req.getClientId())
            .build();
      });
      v = visitRepository.save(
          Visit.builder()
              .id(-1)
              .startTime(req.getStartTime())
              .duration(req.getDuration())
              .animal(animal)
              .status(Status.PLANNED)
              .price(req.getPrice())
              .description(req.getDescription())
              .vet(vet)
              .client(client)
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

    var result = ChronoUnit.MINUTES.between(startTime, LocalDateTime.now());

    return overlaps.isEmpty() && result > 60;
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

  public Optional<Visit> changeVisitStatus(@NotNull int vetId, @NotNull int visitId, @NotNull int status) {
    Status newStatus = Status.values()[status];
    var visit = visitRepository.findById(visitId);
    var vet = vetRepository.findById(vetId);
    if (visit.isEmpty()){
      log.info("visit not found");
      return Optional.empty();
    }

    if (vet.isEmpty()){
      log.info("vet not found");
      return Optional.empty();
    }

    if(visit.get().getVet().getId()!=vetId){
      log.info("this vet is not responsible for this visit");
      return Optional.empty();
    }

    visitRepository.updateStatus(visitId,newStatus);
    return visit;
  }
}
