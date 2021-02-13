package br.com.massao.api.starwars.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private List<ApiFieldError> errors;

    @JsonProperty("debugMessage")
    private String debugMessage;

    private ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex, List<ApiFieldError> errors) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getLocalizedMessage();
        this.errors = errors;
    }


}
