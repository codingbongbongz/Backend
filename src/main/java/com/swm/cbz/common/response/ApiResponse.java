package com.swm.cbz.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final int status;
    private final String message;
    @JsonInclude(NON_NULL)
    private T data;

    private ApiResponse() {
        throw new IllegalStateException();
    }

    public static <T> ApiResponse<T> success(SuccessMessage success) {
        return new ApiResponse<>(success.getHttpStatusCode(), success.getMessage());
    }

    public static <T> ApiResponse<T> success(SuccessMessage success, T data) {
        return new ApiResponse<>(success.getHttpStatusCode(), success.getMessage(), data);
    }

    public static <T> ApiResponse<T> error(ErrorMessage error) {
        return new ApiResponse<>(error.getHttpStatusCode(), error.getMessage());
    }

    public static <T> ApiResponse<T> error(ErrorMessage error, @Nullable String message) {
        return new ApiResponse<>(error.getHttpStatusCode(), message);
    }

    public static <T> ApiResponse<T> error(ErrorMessage error, @Nullable String message, @Nullable T data) {
        return new ApiResponse<>(error.getHttpStatusCode(), message, data);
    }
}
