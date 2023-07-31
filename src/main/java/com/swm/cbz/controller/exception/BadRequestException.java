package com.swm.cbz.controller.exception;

import com.swm.cbz.common.exception.BaseException;
import com.swm.cbz.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class BadRequestException extends BaseException {
    public BadRequestException(ErrorMessage error) {
        super(error, "[Exception] "+ error.getMessage());
    }

}
