package com.swm.cbz.controller.exception;

import com.swm.cbz.common.exception.BaseException;
import com.swm.cbz.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {
    public NotFoundException(ErrorMessage error) {
        super(error, "[NotFoundException] "+ error.getMessage());
    }

}
