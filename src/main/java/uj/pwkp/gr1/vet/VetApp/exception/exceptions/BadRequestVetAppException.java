package uj.pwkp.gr1.vet.VetApp.exception.exceptions;

import lombok.Getter;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;

@Getter
public class BadRequestVetAppException extends RuntimeException{
  private final VetAppResourceType type;

  public BadRequestVetAppException(String message,
      VetAppResourceType type) {
    super(message);
    this.type = type;
  }

  public BadRequestVetAppException(String message, Throwable cause,
      VetAppResourceType type) {
    super(message, cause);
    this.type = type;
  }
}
