package uj.pwkp.gr1.vet.VetApp.entity;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@Builder
@Entity(name = "visit")
@TypeDef(typeClass = PostgreSQLIntervalType.class, defaultForType = Duration.class)
public class Visit extends RepresentationModel<Visit> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private final int id;

  private final LocalDateTime startTime;
  @Column(columnDefinition = "interval")
  private final Duration duration;
  @Column(name = "status")
  private final Status status;
  private final BigDecimal price;
  private String description;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "animal_id", referencedColumnName = "id")
  private Animal animal;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "vet_id", referencedColumnName = "id")
  private Vet vet;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "office_id", referencedColumnName = "id")
  private Office office;

  protected Visit() {
    id = 0;
    startTime = null;
    duration = Duration.ZERO;
    status = Status.PLANNED;
    price = null;
    animal = null;
    client = null;
    vet = null;
    office = null;
  }

}
