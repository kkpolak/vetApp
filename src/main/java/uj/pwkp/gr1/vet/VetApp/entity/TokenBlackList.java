package uj.pwkp.gr1.vet.VetApp.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "tokenBlackList")
@Data
@AllArgsConstructor
@Builder
public class TokenBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Long id;
    private final String token;

    public TokenBlackList() {
        id = 0L;
        token = null;
    }
}
