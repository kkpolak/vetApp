package uj.pwkp.gr1.vet.VetApp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Builder
@Entity
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final int id;
    private final String name;

    protected Office() {
        id = 0;
        name = "-";
    }
}
