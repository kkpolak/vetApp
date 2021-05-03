package uj.pwkp.gr1.vet.VetApp.exception.exceptions;

import lombok.Getter;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.service.VisitCreationResult;

@Getter
public class VisitSystemException extends RuntimeException{
  private final Status visitStatus;
  private final VisitCreationResult visitCreationResult;

  public VisitSystemException(String message, Status visitStatus,
      VisitCreationResult visitCreationResult) {
    super(message);
    this.visitStatus = visitStatus;
    this.visitCreationResult = visitCreationResult;
  }

  public VisitSystemException(String message, Throwable cause,
      Status visitStatus, VisitCreationResult visitCreationResult) {
    super(message, cause);
    this.visitStatus = visitStatus;
    this.visitCreationResult = visitCreationResult;
  }
}
