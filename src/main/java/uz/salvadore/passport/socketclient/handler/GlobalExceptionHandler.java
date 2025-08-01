package uz.salvadore.passport.socketclient.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.asbt.passport.jmrtdlibrary.model.rest.Response;
import uz.salvadore.passport.socketclient.exception.CustomException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<Response<Void>> abstractDomainException(final CustomException ex,
                                                             final WebRequest request) {
    log.error(ex.getMessage());
    return ResponseEntity.badRequest().body(Response.<Void>builder()
      .code(HttpStatus.BAD_REQUEST.value())
      .message(ex.getMessage())
      .build());
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Response<Void>> abstractDomainException(final Exception ex,
                                                                   final WebRequest request) {
    log.error(ex.getMessage());
    return ResponseEntity.badRequest().body(Response.<Void>builder()
      .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
      .message(ex.getMessage())
      .build());
  }

}
