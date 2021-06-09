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
    log.info("Getting visit by id: " + id);
    var result = visitRepository.findById(id);
    return result.orElseThrow(() -> {
      String message = String.format("Wrong id: %s", id);
      log.error(message);
      throw new ObjectNotFoundVetAppException(message,
          VetAppResourceType.VISIT);
    });
  }

  public Visit createVisit(VisitRequest req) {
    if (!dateAvailable(req.getStartTime(), req.getDuration(), req)) {
      String message = "the date is not available";
      log.error(message);
      throw new VisitSystemException(message, Status.NONE,
          VisitCreationResult.OVERLAP);
    }
    Visit v;
    try {
      Vet vet = vetRepository
          .findById(req.getVetId())
          .orElseThrow(() -> {
            String message = String
                .format("Wrong vet id: %s", req.getVetId());
            log.error(message);
            throw new ObjectNotFoundVetAppException(message,
                VetAppResourceType.VET);
          });
      Animal animal = animalRepository
          .findById(req.getAnimalId())
          .orElseThrow(() -> {
            String message = String
                .format("Wrong animal id: %s", req.getAnimalId());
            log.error(message);
            throw new ObjectNotFoundVetAppException(message,
                VetAppResourceType.ANIMAL);
          });
      Client client = clientRepository
          .findById(req.getClientId())
          .orElseThrow(() -> {
            String message = String
                .format("Wrong client id: %s", req.getClientId());
            log.error(message);
            throw new ObjectNotFoundVetAppException(message,
                VetAppResourceType.CLIENT);
          });
      Office office = officeRepository
          .findById(req.getOfficeId())
          .orElseThrow(() -> {
            String message = String
                .format("Wrong office id: %s", req.getOfficeId());
            log.error(message);
            throw new ObjectNotFoundVetAppException(message,
                VetAppResourceType.OFFICE);
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
      String message = "the date is not available or some other unknown error";
      log.error(message);
      System.out.println("@@@@@->" + e.getMessage());
      throw new VisitSystemException(message, Status.PLANNED,
          VisitCreationResult.REPOSITORY_PROBLEM);
    }
    log.info(String.format("Created visit: %s", req.toString()));
    return v;
  }

  public Visit delete(@NotNull int id) {
    var visit = getVisitById(id);
    try {
      visitRepository.delete(visit);
      log.info(String.format("Visit %s deleted", id));
      return visit;
    } catch (Exception e) {
      String message = String.format(
          "An attempt to delete a visit: %s to the database has failed",
          visit);
      log.error(message);
      throw new DeleteVetAppException(message,
          VetAppResourceType.VISIT);
    }
  }

  /**
   * added checking for overlapping visits in the office
   */
  private boolean dateAvailable(LocalDateTime startTime,
      Duration duration, VisitRequest req) {
    List<Visit> overlaps = visitRepository
        .overlaps(startTime,
            startTime.plusMinutes(duration.toMinutes()),
            req.getOfficeId(),
            req.getVetId());
    var result = ChronoUnit.MINUTES
        .between(startTime, LocalDateTime.now());
//    return overlaps.isEmpty() && -result > 60;
    return true;
  }

  public Visit updateVisitStatus(int id, Status status) {
    var visit = getVisitById(id);
    try {
      log.info(String
          .format("Updating visit: %s status to: %s", id, status));
      visitRepository.updateStatus(id, status);
    } catch (Exception e) {
      String message = "updating failed";
      log.error(message);
      throw new VisitSystemException(message, status,
          VisitCreationResult.REPOSITORY_PROBLEM);
    }
    return visit;
  }

  public Visit changeVisitStatus(@NotNull int vetId,
      @NotNull int visitId,
      @NotNull int status) {
    Status newStatus = Status.values()[status];
    var visit = getVisitById(visitId);
    var vet = vetRepository
        .findById(vetId)
        .orElseThrow(() -> {
          String message = String.format("Wrong id: %s", vetId);
          log.error(message);
          throw new ObjectNotFoundVetAppException(message,
              VetAppResourceType.VET);
        });

    if (visit.getVet().getId() != vet.getId()) {
      String message = "this vet is not responsible for this visit";
      log.error(message);
      throw new VisitSystemException(message, Status.NONE,
          VisitCreationResult.COMPATIBILITY_PROBLEM);
    }

    visitRepository.updateStatus(visitId, newStatus);
    return visit;
  }

  public List<LocalDateTime> searchTerms(LocalDateTime start,
      LocalDateTime end, int officeId,
      int vetId) {
    log.info("searching avaliable appointments - visit service");
    List<LocalDateTime> prohibitedTerms = new ArrayList<>();
    var overlapped = visitRepository
        .findVisitsByTimePeriod(start, end, officeId, vetId);
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
