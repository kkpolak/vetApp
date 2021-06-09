package uj.pwkp.gr1.vet.VetApp.entity;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@Builder
@Entity(name = "animal")
public class Animal extends RepresentationModel<Animal> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String name;
  private LocalDateTime dateOfBirth;
  private AnimalType type;

//  @OneToOne(cascade = CascadeType.ALL)
//  @JoinColumn(name = "owner_id", referencedColumnName = "id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Client client;

  protected Animal() {
    id = 0;
    name = "-";
    dateOfBirth = LocalDateTime.now();
    type = null;
  }
}
