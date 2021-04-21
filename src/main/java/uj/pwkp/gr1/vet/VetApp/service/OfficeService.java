package uj.pwkp.gr1.vet.VetApp.service;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.OfficeRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Office;
import uj.pwkp.gr1.vet.VetApp.repository.OfficeRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    public Optional<Office> getOffcieById(int id) {
        return officeRepository.findById(id);
    }

    public Either<String, Office> createOffice(OfficeRequest officeRequest) {
        Office office;
        try {
            office = officeRepository.save(Office.builder()
                .name(officeRequest.getName())
                .build());
        } catch (Exception e) {
            return Either.left("Office creation error");
        }
        return Either.right(office);
    }

    public Optional<Office> deleteOffice(@NotNull int id) {
        var office = officeRepository.findById(id);
        return Optional.ofNullable(office).map(o -> {
            officeRepository.deleteById(id);
            return office;
        }).orElseGet(() -> {
            log.info(String.format("Office with such id: %x was not found", id));
            return Optional.empty();
        });
    }
}
