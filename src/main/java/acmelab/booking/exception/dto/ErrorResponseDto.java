package acmelab.booking.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class ErrorResponseDto {

    private String code;
    private String message;
    private Integer status;
    private Instant timestamp;
    private List<InvalidParameterDto> invalidParameters;

    public ErrorResponseDto(String code, String message, Integer status, List<InvalidParameterDto> invalidParameters) {
        this(code, message, status);
        this.invalidParameters = invalidParameters;
    }

    public ErrorResponseDto(String code, String message, Integer status) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.timestamp = Instant.now();
    }
}
