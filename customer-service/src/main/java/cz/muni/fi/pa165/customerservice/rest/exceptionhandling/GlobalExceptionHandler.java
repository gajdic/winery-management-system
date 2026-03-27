package cz.muni.fi.pa165.customerservice.rest.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.UrlPathHelper;

import java.time.Clock;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(final IllegalArgumentException ex,
                                                          final HttpServletRequest request) {
        HttpStatus status = isNotFound(ex) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(
            LocalDateTime.now(Clock.systemUTC()),
            status,
            ex.getLocalizedMessage(),
            URL_PATH_HELPER.getRequestUri(request));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(final MethodArgumentNotValidException ex,
                                                     final HttpServletRequest request) {
        final String message = ex.getBindingResult().getAllErrors().isEmpty()
            ? "Validation failed"
            : ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        final ApiError apiError = new ApiError(
            LocalDateTime.now(Clock.systemUTC()),
            HttpStatus.BAD_REQUEST,
            message,
            URL_PATH_HELPER.getRequestUri(request));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(final MethodArgumentTypeMismatchException ex,
                                                       final HttpServletRequest request) {
        final ApiError apiError = new ApiError(
            LocalDateTime.now(Clock.systemUTC()),
            HttpStatus.BAD_REQUEST,
            ex.getMostSpecificCause().getLocalizedMessage(),
            URL_PATH_HELPER.getRequestUri(request));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(final Exception ex, final HttpServletRequest request) {
        final ApiError apiError = new ApiError(
            LocalDateTime.now(Clock.systemUTC()),
            HttpStatus.INTERNAL_SERVER_ERROR,
            getInitialException(ex).getLocalizedMessage(),
            URL_PATH_HELPER.getRequestUri(request));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private boolean isNotFound(IllegalArgumentException ex) {
        String message = ex.getMessage();
        return message != null && message.toLowerCase().contains("not found");
    }

    private Exception getInitialException(Exception ex) {
        while (ex.getCause() != null) {
            ex = (Exception) ex.getCause();
        }
        return ex;
    }
}
