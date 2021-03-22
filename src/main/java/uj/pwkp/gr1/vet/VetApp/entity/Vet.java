package uj.pwkp.gr1.vet.VetApp.entity;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
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
@Entity(name = "vet")
@TypeDef(typeClass = PostgreSQLIntervalType.class, defaultForType = Duration.class)
public class Vet {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private final int id;

  private final String firstName;
  private final String lastName;
  private final String photo;

  private final LocalDateTime admissionStart;
  private final LocalDateTime admissionEnd;
  @Column(columnDefinition = "interval")
  private final Duration duration;

  protected Vet() {
    id = 0;
    firstName = "-";
    lastName = "-";
    photo = "-";
    admissionEnd = null;
    admissionStart = null;
    duration = Duration.ZERO;
  }

}
