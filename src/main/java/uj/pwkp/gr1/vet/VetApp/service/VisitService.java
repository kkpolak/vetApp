package uj.pwkp.gr1.vet.VetApp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

import io.vavr.control.Either;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.SearchRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VisitRequest;
import uj.pwkp.gr1.vet.VetApp.entity.*;
import uj.pwkp.gr1.vet.VetApp.repository.*;

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

  @Autowired
  private OfficeRepository officeRepository;

  public List<Visit> getAllVisits() {
    return visitRepository.findAll();
  }

  public Either<VisitCreationResult, Visit> createVisit(VisitRequest req) {
    if (!dateAvailable(req.getStartTime(), req.getDuration(), req)) {
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
      Office office = officeRepository.findById(req.getOfficeId()).orElseGet(() -> {
        log.info(String.format("office with this id: %x was not found", req.getOfficeId()));
        return Office.builder().name("anonymus").id(req.getOfficeId())
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
              .office(office)
              .build());
    } catch (Exception e) {
      return Either.left(VisitCreationResult.REPOSITORY_PROBLEM);
    }
    return Either.right(v);
  }

  /*
  *    added checking for overlapping visits in the office
   */
  private boolean dateAvailable(LocalDateTime startTime, Duration duration, VisitRequest req) {
    List<Visit> overlaps = visitRepository
        .overlaps(startTime, startTime.plusMinutes(duration.toMinutes()), req.getOfficeId(), req.getVetId());
    overlaps.forEach(x -> log.info(x.toString()));
    var result = ChronoUnit.MINUTES.between(startTime, LocalDateTime.now());
    return overlaps.isEmpty() && -result > 60;
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

  public List<LocalDateTime> searchTerms(LocalDateTime start, LocalDateTime end, int officeId, int vetId) {
    List<LocalDateTime> prohibitedTerms = new ArrayList<>();
    var overlapped = visitRepository.findVisitsByTimePeriod(start, end, officeId, vetId);
    overlapped.forEach(x -> {
          prohibitedTerms.add(x.getStartTime());
          prohibitedTerms.add(x.getStartTime().plus(x.getDuration()));
        });
    List<LocalDateTime> slots = new ArrayList<>() ;
    LocalDateTime x = start;
    int index = 0;
    while (x.isBefore(end)) {

      boolean endLoop = index > prohibitedTerms.size() - 1;
      if(!endLoop && x.isBefore(prohibitedTerms.get(index))) {
        slots.add(x);
        x = x.plusMinutes(15);
      } else if(endLoop) {
        break;
      } else {
        x = prohibitedTerms.get(index + 1);
        index += 2;
      }
    }
    while (x.isBefore(end)) {
      slots.add(x);
      x = x.plusMinutes(15);
    }
    return slots;
  }
}
