package uj.pwkp.gr1.vet.VetApp;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalUnit;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.AnimalType;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.entity.Office;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.entity.Vet;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;

public class BaseTestObjects {

  public static Animal animal1;
  public static Animal animal2;
  public static Client client1;
  public static Client client2;
  public static Office office1;
  public static Office office2;
  public static Vet vet1;
  public static Vet vet2;
  public static Visit visit1;
  public static Visit visit2;

  public static void initAnimals() {
    animal1 = Animal.builder()
        .id(1)
        .name("animal1")
        .owner(client1)
        .type(AnimalType.DOG)
        .dateOfBirth(LocalDateTime.of(2020, Month.APRIL, 20, 19, 31, 29))
        .build();

    animal2 = Animal.builder()
        .id(20)
        .name("animal2")
        .owner(client2)
        .type(AnimalType.CAT)
        .dateOfBirth(LocalDateTime.of(2017, Month.APRIL, 11, 11, 42, 47))
        .build();
  }

  public static void initClients() {
    client1 = Client.builder()
        .id(1)
        .firstName("client1F")
        .lastName("client1L")
        .build();

    client2 = Client.builder()
        .id(17)
        .firstName("client2F")
        .lastName("client2L")
        .build();
  }

  public static void initVets() {
    vet1 = Vet.builder()
        .id(1)
        .firstName("vet1F")
        .lastName("vet1L")
        .photo("photo1")
        .duration(Duration.parse("PT8H"))
        .admissionEnd(LocalDateTime.of(2021, Month.JANUARY, 1, 8, 0))
        .admissionStart(LocalDateTime.of(2021, Month.JANUARY, 1, 16, 0))
        .build();

    vet2 = Vet.builder()
        .id(12)
        .firstName("vet2F")
        .lastName("vet2L")
        .photo("photo2")
        .duration(Duration.parse("PT5H"))
        .admissionEnd(LocalDateTime.of(2021, Month.APRIL, 1, 9, 0))
        .admissionStart(LocalDateTime.of(2021, Month.APRIL, 1, 14, 0))
        .build();
  }

  public static void initOffices() {
    office1 = Office.builder()
        .id(1)
        .name("office1")
        .build();

    office2 = Office.builder()
        .id(19)
        .name("office2")
        .build();
  }

  public static void initVisits() {
    visit1 = Visit.builder()
        .id(1)
        .client(client1)
        .animal(animal1)
        .vet(vet1)
        .office(office1)
        .description("mess1")
        .status(Status.PLANNED)
        .price(BigDecimal.ONE)
        .duration(Duration.parse("PT0H15M"))
        .startTime(LocalDateTime.of(2021, Month.APRIL, 14, 10, 15))
        .build();

    visit2 = Visit.builder()
        .id(15)
        .client(client2)
        .animal(animal2)
        .vet(vet2)
        .office(office2)
        .description("mess2")
        .status(Status.FINISHED_AUTOMATICALLY)
        .price(BigDecimal.TEN)
        .duration(Duration.parse("PT0H30M"))
        .startTime(LocalDateTime.of(2021, Month.MARCH, 17, 11, 30))
        .build();
  }
}
