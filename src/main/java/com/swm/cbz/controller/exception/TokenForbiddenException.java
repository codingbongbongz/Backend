package com.swm.cbz.controller.exception;

import com.swm.cbz.common.exception.BaseException;
import com.swm.cbz.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class TokenForbiddenException extends BaseException {
    public TokenForbiddenException(ErrorMessage error) {
        super(error, "[TokenForbiddenException] "+ error.getMessage());
    }

}
