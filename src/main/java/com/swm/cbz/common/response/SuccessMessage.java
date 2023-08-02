package com.swm.cbz.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessMessage {
    /**
     * auth
     */
    LOGIN_SUCCESS(CREATED, "로그인이 성공했습니다."),

    /**
     * user
     */
    GET_VIDEOS_BY_USER_SUCCESS(OK, "유저별 동영상을 불러오는데 성공했습니다."),

    /**
     * video
     */
    CREATE_VIDEO_SUCCESS(CREATED, "비디오 생성에 성공했습니다."),
    GET_POPULAR_VIDEO_SUCCESS(OK, "인기 동영상 조회에 성공했습니다."),

    /**
     * transcript
     */
    GET_TRANSCRIPT_SUCCESS(OK, "자막을 불러오는데 성공했습니다."),

    EVALUATION_SUCCESS(OK, "발음을 분석하는데 성공했습니다."),

    GET_TRANSCRIPT_AUDIO_SUCCESS(OK, "자막 음성을 불러오는데 성공했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
