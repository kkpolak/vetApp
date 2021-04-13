package uj.pwkp.gr1.vet.VetApp.entity;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Entity(name = "animal")
public class Animal {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String name;
  private LocalDateTime dateOfBirth;
  private AnimalType type;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "owner_id", referencedColumnName = "id")
  private Client owner;

  protected Animal() {
    id = 0;
    name = "-";
    dateOfBirth = LocalDateTime.now();
    type = null;
  }
}
