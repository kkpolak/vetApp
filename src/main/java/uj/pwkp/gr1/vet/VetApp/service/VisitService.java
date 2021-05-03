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
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.ObjectNotFoundVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.VisitSystemException;
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
    log.info("Getting all visits - service");
    return visitRepository.findAll();
  }

  public Visit getVisitById(@NotNull int id) {
    var result = visitRepository.findById(id);
    return result.orElseThrow(
        () -> new ObjectNotFoundVetAppException(String.format("Wrong id: %s", id),
            VetAppResourceType.VISIT));
  }

  public Visit createVisit(VisitRequest req) {
    if (!dateAvailable(req.getStartTime(), req.getDuration(), req)) {
      throw new VisitSystemException("the date is not available", Status.NONE,
          VisitCreationResult.OVERLAP);
    }
    Visit v;
    try {
      Vet vet = vetRepository.findById(req.getVetId()).orElseThrow(
          () -> new ObjectNotFoundVetAppException(String.format("Wrong id: %s", req.getVetId()),
              VetAppResourceType.VET));
      Animal animal = animalRepository.findById(req.getAnimalId()).orElseThrow(
          () -> new ObjectNotFoundVetAppException(String.format("Wrong id: %s", req.getAnimalId()),
              VetAppResourceType.ANIMAL));
      Client client = clientRepository.findById(req.getClientId()).orElseThrow(
          () -> new ObjectNotFoundVetAppException(String.format("Wrong id: %s", req.getClientId()),
              VetAppResourceType.CLIENT));
      Office office = officeRepository.findById(req.getOfficeId()).orElseThrow(
          () -> new ObjectNotFoundVetAppException(String.format("Wrong id: %s", req.getOfficeId()),
              VetAppResourceType.OFFICE));
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
      throw new VisitSystemException("the date is not available", Status.PLANNED,
          VisitCreationResult.REPOSITORY_PROBLEM);
    }
    return v;
  }

  public Visit delete(@NotNull int id) {
    var visit = getVisitById(id);
    try {
      visitRepository.delete(visit);
      return visit;
    } catch (Exception e) {
      throw new DeleteVetAppException(
          String.format("An attempt to add a visit: %s to the database has failed", visit),
          VetAppResourceType.VISIT);
    }
  }

  /**
   * added checking for overlapping visits in the office
   */
  private boolean dateAvailable(LocalDateTime startTime, Duration duration, VisitRequest req) {
    List<Visit> overlaps = visitRepository
        .overlaps(startTime, startTime.plusMinutes(duration.toMinutes()), req.getOfficeId(),
            req.getVetId());
    overlaps.forEach(x -> log.info(x.toString()));
    var result = ChronoUnit.MINUTES.between(startTime, LocalDateTime.now());
    return overlaps.isEmpty() && -result > 60;
  }

  public Visit updateVisitStatus(int id, Status status) {
    var visit = getVisitById(id);
    try {
      log.info("Updating DB");
      visitRepository.updateStatus(id, status);
    } catch (Exception e) {
      throw new VisitSystemException("updating failed", status,
          VisitCreationResult.REPOSITORY_PROBLEM);
    }
    return visit;
  }

  public Visit changeVisitStatus(@NotNull int vetId, @NotNull int visitId,
      @NotNull int status) {
    Status newStatus = Status.values()[status];
    var visit = getVisitById(visitId);
    var vet = vetRepository.findById(vetId).orElseThrow(
        () -> new ObjectNotFoundVetAppException(String.format("Wrong id: %s", vetId),
            VetAppResourceType.VET));

    if (visit.getVet().getId() != vet.getId()) {
      throw new VisitSystemException("this vet is not responsible for this visit", Status.NONE,
          VisitCreationResult.COMPATIBILITY_PROBLEM);
    }

    visitRepository.updateStatus(visitId, newStatus);
    return visit;
  }

  public List<LocalDateTime> searchTerms(LocalDateTime start, LocalDateTime end, int officeId,
      int vetId) {
    List<LocalDateTime> prohibitedTerms = new ArrayList<>();
    var overlapped = visitRepository.findVisitsByTimePeriod(start, end, officeId, vetId);
    overlapped.forEach(x -> {
      prohibitedTerms.add(x.getStartTime());
      prohibitedTerms.add(x.getStartTime().plus(x.getDuration()));
    });
    List<LocalDateTime> slots = new ArrayList<>();
    LocalDateTime time = start;
    int index = 0;
    while (time.isBefore(end)) {

      boolean endLoop = index > prohibitedTerms.size() - 1;
      if (!endLoop && time.isBefore(prohibitedTerms.get(index))) {
        slots.add(time);
        time = time.plusMinutes(15);
      } else if (endLoop) {
        break;
      } else {
        time = prohibitedTerms.get(index + 1);
        index += 2;
      }
    }
    while (time.isBefore(end)) {
      slots.add(time);
      time = time.plusMinutes(15);
    }
    return slots;
  }
}
