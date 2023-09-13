package com.swm.cbz.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

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

    TRANSCRIPT_NOT_FOUND(NOT_FOUND, "자막을 찾을 수 없습니다."),

    /**
     * OpenAI
     */


    OPENAI_FAILURE(OK, "OpenAI response 가져오기 실패")
    ;
    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
