package cz.muni.fi.pa165.productionservice.api;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class ValidationErrorDto extends ApiErrorDto {
    private Map<String, String> fieldErrors = new LinkedHashMap<>();

    public ValidationErrorDto() {
    }

    public ValidationErrorDto(Instant timestamp, int status, String error, String message, String path, Map<String, String> fieldErrors) {
        super(timestamp, status, error, message, path);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
