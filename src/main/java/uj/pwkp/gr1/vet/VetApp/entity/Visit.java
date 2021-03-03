package uj.pwkp.gr1.vet.VetApp.entity;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

@Data
@AllArgsConstructor
@Builder
@Entity(name = "visits")
@TypeDef(typeClass = PostgreSQLIntervalType.class, defaultForType = Duration.class)
public class Visit {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private final int id;

  private final LocalDateTime startTime;
  @Column(columnDefinition = "interval")
  private final Duration duration;
  @Column(name = "animal")
  private final Animal animal;
  @Column(name = "status")
  private final Status status;
  private final BigDecimal price;

  protected Visit() {
    id = 0;
    startTime = null;
    duration = Duration.ZERO;
    animal = Animal.OTHER;
    status = Status.PLANNED;
    price = null;
  }

  public static Visit newVisit(LocalDateTime startTime, Duration duration, Animal animal,
      BigDecimal price) {
    return new Visit(-1, startTime, duration, animal, Status.PLANNED, price);
  }
}
