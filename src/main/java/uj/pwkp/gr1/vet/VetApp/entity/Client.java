package uj.pwkp.gr1.vet.VetApp.entity;

import java.time.Duration;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Entity(name = "client")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private final int id;

  private final String firstName;
  private final String lastName;

  protected Client() {
    id = 0;
    firstName = "-";
    lastName = "-";
  }

  //private final List<Animal> animals;

//  @OneToOne(mappedBy = "animal")
//  Animal animal;
}
