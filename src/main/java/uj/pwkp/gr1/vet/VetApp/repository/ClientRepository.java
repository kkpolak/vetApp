package uj.pwkp.gr1.vet.VetApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.pwkp.gr1.vet.VetApp.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

}
