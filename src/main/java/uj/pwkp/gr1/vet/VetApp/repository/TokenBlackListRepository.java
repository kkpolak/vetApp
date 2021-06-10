package uj.pwkp.gr1.vet.VetApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.pwkp.gr1.vet.VetApp.entity.TokenBlackList;

@Repository
public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Integer> {
    TokenBlackList findTokenBlackListByTokenEquals(String token);
}
