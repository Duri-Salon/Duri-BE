package kr.com.duri.common.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
public class CommonResponseEntity<T> {
    private final boolean success;
    private final T response;
    private final CustomError error;

    public CommonResponseEntity(boolean success, T response, CustomError error) {
        this.success = success;
        this.response = response;
        this.error = error;
    }

    public static <T> CommonResponseEntity<T> success(T response) {
        return new CommonResponseEntity<>(true, response, null);
    }

    public static <T> CommonResponseEntity<T> error(HttpStatus status, String message) {
        return new CommonResponseEntity<>(false, null, new CustomError(message, status));
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("response", response);
        result.put("error", error);
        return result;
    }
}
