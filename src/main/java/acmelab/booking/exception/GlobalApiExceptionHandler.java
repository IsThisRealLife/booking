package acmelab.booking.exception;

import acmelab.booking.exception.dto.ErrorResponseDto;
import acmelab.booking.exception.dto.InvalidParameterDto;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleUncaughtException(final Exception ex) {

        log.error("Request failed with exception: {}", ex.getMessage(), ex);
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                Exception.class.getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleCustomUncaughtBusinessException(final BusinessException ex) {
        log.error("Request failed with exception: {}", ex.getMessage(), ex);
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                ex.getCode(),
                ex.getMessage(),
                ex.getHttpStatus().value());

        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponseDto);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex) {
        log.error("Request failed with exception: {}", ex.getMessage(), ex);

        final List<InvalidParameterDto> invalidParameters = new ArrayList<>();
        ex.getConstraintViolations().forEach(constraintViolation -> {
            final Iterator<Path.Node> it = constraintViolation.getPropertyPath().iterator();
            if (it.hasNext()) {
                try {
                    it.next();
                    final Path.Node n = it.next();
                    final InvalidParameterDto invalidParameter = new InvalidParameterDto();
                    invalidParameter.setParameter(n.getName());
                    invalidParameter.setMessage(constraintViolation.getMessage());
                    invalidParameters.add(invalidParameter);
                } catch (final Exception e) {
                    log.warn("Failed to extract invalid parameter", e);
                }
            }
        });

        final ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                ConstraintViolationException.class.getSimpleName(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                invalidParameters);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.error("Request failed with exception: {}", ex.getMessage(), ex);

        final List<InvalidParameterDto> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> InvalidParameterDto.builder()
                        .parameter(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build()).toList();

        if(!fieldErrors.isEmpty()) {
            final ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                    MethodArgumentNotValidException.class.getSimpleName(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(),
                    fieldErrors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
        }


        String globalError = ex.getBindingResult().getGlobalErrors().stream()
                .findFirst().map(error -> error.getDefaultMessage()).orElse(null);

        final ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                MethodArgumentNotValidException.class.getSimpleName(),
                globalError,
                HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {

        final List<InvalidParameterDto> invalidParameters = List.of(InvalidParameterDto.builder()
                .parameter(ex.getPropertyName())
                .message("Failed to convert '" + ex.getPropertyName() + "' with value: '" + ex.getValue() + "'").build());

        final ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                TypeMismatchException.class.getSimpleName(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                invalidParameters);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
}
