package uj.pwkp.gr1.vet.VetApp.exception;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.BadRequestVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.CreateVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.ObjectNotFoundVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.VisitSystemException;

@RestControllerAdvice(annotations = RestController.class)
public class VetAppExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(value = {ObjectNotFoundVetAppException.class})
  public ResponseEntity<Object> handleVetAppException(ObjectNotFoundVetAppException e) {
    var vetAppExceptionResponse = VetAppExceptionResponse.builder()
        .message(e.getMessage())
        .throwable(e)
        .httpStatus(HttpStatus.NOT_FOUND)
        .timeStamp(ZonedDateTime.now())
        .type(e.getType())
        .build();
    return new ResponseEntity<>(vetAppExceptionResponse, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = {CreateVetAppException.class})
  public ResponseEntity<Object> handleVetAppException(CreateVetAppException e) {
    var vetAppExceptionResponse = VetAppExceptionResponse.builder()
        .message(e.getMessage())
        .throwable(e)
        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        .timeStamp(ZonedDateTime.now())
        .type(e.getType())
        .build();
    return new ResponseEntity<>(vetAppExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = {DeleteVetAppException.class})
  public ResponseEntity<Object> handleVetAppException(DeleteVetAppException e) {
    var vetAppExceptionResponse = VetAppExceptionResponse.builder()
        .message(e.getMessage())
        .throwable(e)
        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        .timeStamp(ZonedDateTime.now())
        .type(e.getType())
        .build();
    return new ResponseEntity<>(vetAppExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {BadRequestVetAppException.class})
  public ResponseEntity<Object> handleVetAppException(BadRequestVetAppException e) {
    var vetAppExceptionResponse = VetAppExceptionResponse.builder()
        .message(e.getMessage())
        .throwable(e)
        .httpStatus(HttpStatus.BAD_REQUEST)
        .timeStamp(ZonedDateTime.now())
        .type(e.getType())
        .build();
    return new ResponseEntity<>(vetAppExceptionResponse, HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {VisitSystemException.class})
  public ResponseEntity<Object> handleVetAppException(VisitSystemException e) {
    var visitExceptionResponse = VisitSystemExceptionResponse.builder()
        .message(e.getMessage())
        .throwable(e)
        .httpStatus(HttpStatus.BAD_REQUEST)
        .timeStamp(ZonedDateTime.now())
        .visitCreationResult(e.getVisitCreationResult())
        .visitStatus(e.getVisitStatus())
        .build();
    return new ResponseEntity<>(visitExceptionResponse, HttpStatus.BAD_REQUEST);
  }

}
