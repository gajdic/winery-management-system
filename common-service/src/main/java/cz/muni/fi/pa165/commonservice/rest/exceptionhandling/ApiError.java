package cz.muni.fi.pa165.commonservice.rest.exceptionhandling;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(description = "Standard API error response returned by all services")
public class ApiError {

    @Schema(description = "Server-side timestamp when the error was produced", example = "2026-05-22T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status of the error", example = "404 NOT_FOUND")
    private HttpStatus status;

    @Schema(description = "Human-readable message describing the error", example = "Resource not found: 44444444-4444-4444-4444-444444444444")
    private String message;

    @Schema(description = "Request path that produced the error", example = "/customers/44444444-4444-4444-4444-444444444444")
    private String path;

    public ApiError() {
    }

    public ApiError(LocalDateTime timestamp, HttpStatus status, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
