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
    var result = officeRepository.findById(id);
    return result.orElseThrow(
        () -> new ObjectNotFoundVetAppException(String.format("Wrong id: %s", id),
            VetAppResourceType.OFFICE));
  }

  public Office createOffice(OfficeRequest officeRequest) {
    Office office;
    try {
      office = officeRepository.save(Office.builder()
          .name(officeRequest.getName())
          .build());
    } catch (Exception e) {
      throw new CreateVetAppException(
          String.format("An attempt to add a office: %s to the database has failed",
              officeRequest.toString()), VetAppResourceType.ANIMAL);
    }
    return office;
  }

  public Office deleteOffice(@NotNull int id) {
    var office = getOfficeById(id);
    try {
      officeRepository.delete(office);
      return office;
    } catch (Exception e) {
      throw new DeleteVetAppException(
          String.format("An attempt to add a office: %s to the database has failed", office),
          VetAppResourceType.ANIMAL);
    }
  }
}
