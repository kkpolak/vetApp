package uj.pwkp.gr1.vet.VetApp.exception;


import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class VetAppExceptionResponse {
  private final String message;
  private final Throwable throwable;
  private final HttpStatus httpStatus;
  private final ZonedDateTime timeStamp;
  private final VetAppResourceType type;
}
