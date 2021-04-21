package uj.pwkp.gr1.vet.VetApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.pwkp.gr1.vet.VetApp.entity.Office;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {
}
