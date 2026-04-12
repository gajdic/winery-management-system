package cz.muni.fi.pa165.commonservice.rest.exceptionhandling;

import cz.muni.fi.pa165.commonservice.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.UrlPathHelper;

import java.time.Clock;
import java.time.LocalDateTime;

public abstract class BaseGlobalExceptionHandler {

    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
        final ResourceNotFoundException ex,
        final HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
        final IllegalArgumentException ex,
        final HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
        final MethodArgumentNotValidException ex,
        final HttpServletRequest request
    ) {
        final String message = ex.getBindingResult().getAllErrors().isEmpty()
            ? "Validation failed"
            : ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(
        final MethodArgumentTypeMismatchException ex,
        final HttpServletRequest request
    ) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMostSpecificCause().getLocalizedMessage(),
            request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(
        final Exception ex,
        final HttpServletRequest request
    ) {
        return buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            getInitialException(ex).getLocalizedMessage(),
            request
        );
    }

    protected ResponseEntity<ApiError> buildErrorResponse(
        final HttpStatus status,
        final String message,
        final HttpServletRequest request
    ) {
        final ApiError apiError = new ApiError(
            LocalDateTime.now(Clock.systemUTC()),
            status,
            message,
            URL_PATH_HELPER.getRequestUri(request)
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private Exception getInitialException(Exception ex) {
        while (ex.getCause() instanceof Exception cause) {
            ex = cause;
        }
        return ex;
    }
}
