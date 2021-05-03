package uj.pwkp.gr1.vet.VetApp.service;

import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VetRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.entity.Vet;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.CreateVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.ObjectNotFoundVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.VisitSystemException;
import uj.pwkp.gr1.vet.VetApp.repository.VetRepository;
import uj.pwkp.gr1.vet.VetApp.repository.VisitRepository;

@Slf4j
@Service
public class VetService {

  @Autowired
  private VetRepository vetRepository;

  @Autowired
  private VisitRepository visitRepository;

  public List<Vet> getAllVets() {
    log.info("Getting all vets - service");
    return vetRepository.findAll();
  }

  public Vet getVetById(int id) {
    var result = vetRepository.findById(id);
    log.info("Getting vet by id: " + id);
    return result.orElseThrow(
        () -> new ObjectNotFoundVetAppException(String.format("Wrong id: %s", id),
            VetAppResourceType.VET));
  }

  public Vet createVet(VetRequest req) {
    Vet v;
    try {
      v = vetRepository.save(
          Vet.builder()
              .id(-1)
              .firstName(req.getFirstName())
              .lastName(req.getLastName())
              .admissionStart(req.getAdmissionStart())
              .admissionEnd(req.getAdmissionEnd())
              .duration(req.getDuration())
              .photo(req.getPhoto())
              .build());
    } catch (Exception e) {
      throw new CreateVetAppException(
          String
              .format("An attempt to add a vet: %s to the database has failed", req.toString()),
          VetAppResourceType.VET);
    }

    return v;
  }

  public Vet delete(@NotNull int id) {
    var vet = getVetById(id);
    try {
      vetRepository.delete(vet);
      return vet;
    } catch (Exception e) {
      throw new DeleteVetAppException(
          String.format("An attempt to add a vet: %s to the database has failed", vet),
          VetAppResourceType.VET);
    }
  }

  public Visit changeVisitDescription(@NotNull int vetId, @NotNull int visitId,
      @NotNull String description) {
    var visit = visitRepository.findById(visitId).orElseThrow(
        () -> new ObjectNotFoundVetAppException(String.format("Wrong id: %s", visitId),
            VetAppResourceType.VISIT));
    var vet = getVetById(vetId);

    if (visit.getVet().getId() != vet.getId()) {
      throw new VisitSystemException("this vet is not responsible for this visit", Status.NONE,
          VisitCreationResult.COMPATIBILITY_PROBLEM);
    }

    if (visit.getStatus() != Status.PLANNED
        || visit.getStatus() != Status.FINISHED_SUCCESS) {
      throw new VisitSystemException(
          "you cannot change the description of a visit with this status",
          visit.getStatus(), VisitCreationResult.COMPATIBILITY_PROBLEM);
    }

    visitRepository.updateDescription(visitId, description);
    return visit;
  }

}
