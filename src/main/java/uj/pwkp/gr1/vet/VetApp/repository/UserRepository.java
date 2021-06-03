package uj.pwkp.gr1.vet.VetApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uj.pwkp.gr1.vet.VetApp.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
