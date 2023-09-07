package com.swm.cbz.controller.exception;

import com.swm.cbz.common.exception.BaseException;
import com.swm.cbz.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class UserConflictException extends BaseException {
    public UserConflictException(ErrorMessage error) {
        super(error, "[UserConflictException] "+ error.getMessage());
    }

}
