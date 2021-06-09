package uj.pwkp.gr1.vet.VetApp.entity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@Builder
@Entity(name = "client")
public class Client extends RepresentationModel<Client> {

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

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Animal> animals = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Visit> visits = new ArrayList<>();

//  @OneToOne(mappedBy = "animal")
//  Animal animal;
}
