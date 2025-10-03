package me.therimuru.RestAuth.object;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {

    private final HttpStatus httpStatus;
    private final Map<String, Object> fields = new HashMap<>();

    private ApiResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ApiResponse field(String key, Object value) {
        fields.put(key, value);
        return this;
    }

    public ResponseEntity<Map<String, Object>> build() {
        fields.put("status",
                switch (httpStatus.series().value()) {
                    case 1 -> "Info";
                    case 2 -> "Success";
                    case 3 -> "Redirection";
                    case 4 -> "Client error";
                    case 5 -> "Internal server error";
                    default -> "Undefined";
                }
        );
        return new ResponseEntity<>(fields, httpStatus);
    }

    public static ApiResponse builder(HttpStatus httpStatus) {
        return new ApiResponse(httpStatus);
    }
}