package com.swm.cbz.controller.exception;

import com.swm.cbz.common.exception.BaseException;
import com.swm.cbz.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(ErrorMessage error) {
        super(error, "[UnauthorizedException] "+ error.getMessage());
    }

}
