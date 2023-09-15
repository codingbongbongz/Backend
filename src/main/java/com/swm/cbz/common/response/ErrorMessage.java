package com.swm.cbz.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {
    /**
     * auth
     */
    AUTHENTICATION_BEARER_EXCEPTION(UNAUTHORIZED,"Bearer로 시작하지 않는 토큰입니다."),
    EXPIRED_ALL_TOKEN_EXCEPTION(FORBIDDEN, "토큰이 모두 만료되었습니다. 다시 로그인해주세요."),
    VALID_ALL_TOKEN_EXCEPTION(FORBIDDEN, "토큰이 모두 유효합니다."),

    /**
     * user
     */
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_FOUND_USER_EXCEPTION(NOT_FOUND, "유저를 찾을 수 없습니다."),

    NOT_FOUND_EVALUATION_EXCEPTION(NOT_FOUND, "분석을 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY_EXCEPTION(NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    CONFLICT_USER_PASSWORD_EXCEPTION(CONFLICT, "존재하는 유저 비밀번호 입니다."),
    CONFLICT_USER_NICKNAME_EXCEPTION(CONFLICT, "존재하는 유저 닉네임 입니다."),


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
