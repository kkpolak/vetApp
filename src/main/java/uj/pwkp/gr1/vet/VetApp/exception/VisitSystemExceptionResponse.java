package uj.pwkp.gr1.vet.VetApp.exception;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.service.VisitCreationResult;

@Data
@Builder
@AllArgsConstructor
public class VisitSystemExceptionResponse {
  private final String message;
  private final Throwable throwable;
  private final HttpStatus httpStatus;
  private final ZonedDateTime timeStamp;
  private final Status visitStatus;
  private final VisitCreationResult visitCreationResult;
}
