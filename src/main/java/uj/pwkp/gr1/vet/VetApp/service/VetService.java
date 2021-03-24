package uj.pwkp.gr1.vet.VetApp.service;

import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VetRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Vet;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.repository.VetRepository;

@Slf4j
@Service
public class VetService {

  @Autowired
  private VetRepository vetRepository;

  public List<Vet> getAllVets() {
    return vetRepository.findAll();
  }

  public Optional<Vet> getVetById(int id) {
    return vetRepository.findById(id);
  }

  public Either<String, Vet> createVet(VetRequest req) {
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
      return Either.left("vet creation error");
    }

    return Either.right(v);
  }

  public Optional<Vet> delete(@NotNull int id) {
    var vet = vetRepository.findById(id);
    return Optional.ofNullable(vet).map(v -> {
      vetRepository.deleteById(v.get().getId());
      return v;
    }).orElseGet(() -> {
      log.info(String.format("Vet with such id: %x was not found", id));
      return Optional.empty();
    });
  }
}
