package com.guhan.formcraft.dto;

import com.guhan.formcraft.models.ValidationError;
import java.util.List;

public class ValidationResponseDTO {
    private boolean success;
    private String message;
    private List<ValidationError> errors;

    public ValidationResponseDTO() {}

    public ValidationResponseDTO(boolean success, String message, List<ValidationError> errors) {
        this.success = success;
        this.message = message;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }
}
