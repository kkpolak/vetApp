package uj.pwkp.gr1.vet.VetApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.OfficeRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Office;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.CreateVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.ObjectNotFoundVetAppException;
import uj.pwkp.gr1.vet.VetApp.repository.OfficeRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Service
public class OfficeService {

  @Autowired
  private OfficeRepository officeRepository;

  public List<Office> getAllOffices() {
    log.info("Getting all offices - service");
    return officeRepository.findAll();
  }

  public Office getOfficeById(int id) {
    log.info("Getting office by id: " + id);
    var result = officeRepository.findById(id);
    return result.orElseThrow(() -> {
          String message = String.format("Wrong id: %s", id);
          log.error(message);
          throw new ObjectNotFoundVetAppException(message, VetAppResourceType.OFFICE);
        });
  }

  public Office createOffice(OfficeRequest officeRequest) {
    Office office;
    try {
      office = officeRepository.save(Office.builder()
          .name(officeRequest.getName())
          .build());
    } catch (Exception e) {
      String message = String.format("An attempt to add a office: %s to the database has failed", officeRequest.toString());
      log.error(message);
      throw new CreateVetAppException(message, VetAppResourceType.ANIMAL);
    }
    log.info(String.format("Office %s created", officeRequest.toString()));
    return office;
  }

  public Office deleteOffice(@NotNull int id) {
    var office = getOfficeById(id);
    try {
      officeRepository.delete(office);
      log.info(String.format("Office %s deleted", office));
      return office;
    } catch (Exception e) {
      String message = String.format("An attempt to add a office: %s to the database has failed", office);
      log.error(message);
      throw new DeleteVetAppException(message, VetAppResourceType.ANIMAL);
    }
  }
}
