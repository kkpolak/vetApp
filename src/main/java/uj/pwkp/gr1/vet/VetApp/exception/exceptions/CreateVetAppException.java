package uj.pwkp.gr1.vet.VetApp.exception.exceptions;

import lombok.Getter;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;

@Getter
public class CreateVetAppException extends RuntimeException{
  private final VetAppResourceType type;

  public CreateVetAppException(String message,
      VetAppResourceType type) {
    super(message);
    this.type = type;
  }

  public CreateVetAppException(String message, Throwable cause,
      VetAppResourceType type) {
    super(message, cause);
    this.type = type;
  }
}
