package com.swm.cbz.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {
    /**
     * auth
     */


    /**
     * user
     */
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_FOUND_USER_EXCEPTION(NOT_FOUND, "유저를 찾을 수 없습니다."),

    NOT_FOUND_CATEGORY_EXCEPTION(NOT_FOUND, "카테고리를 찾을 수 없습니다."),


    /**
     * video
     */


    /**
     * server
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),


    /**
     * transcript
     */

    TRANSCRIPT_NOT_FOUND(NOT_FOUND, "자막을 찾을 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
