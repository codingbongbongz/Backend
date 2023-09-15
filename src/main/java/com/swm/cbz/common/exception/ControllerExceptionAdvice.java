package com.swm.cbz.common.exception;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.controller.exception.BadRequestException;
import com.swm.cbz.controller.exception.NotFoundException;
import com.swm.cbz.controller.exception.TokenForbiddenException;
import com.swm.cbz.controller.exception.UnauthorizedException;
import com.swm.cbz.controller.exception.UserConflictException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    /**
     * 400 BAD REQUEST
     */
    @ExceptionHandler({
            BadRequestException.class
    })
    public ResponseEntity<ApiResponse> BadRequestException(BaseException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }

    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ApiResponse> NotFoundException(BaseException exception) {
        return ResponseEntity.status(NOT_FOUND).body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }

    /**
     * 401 UNAUTHORIZED
     */
    @ExceptionHandler({
        UnauthorizedException.class
    })
    public ResponseEntity<ApiResponse> UnauthorizedException(BaseException exception) {
        return ResponseEntity.status(UNAUTHORIZED)
            .body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }

    /**
     * 403 FORBIDDEN
     */
    @ExceptionHandler({
        TokenForbiddenException.class
    })
    public ResponseEntity<ApiResponse> ForbiddenException(BaseException exception) {
        return ResponseEntity.status(FORBIDDEN)
            .body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }


    /**
     * 404 NOT FOUND
     */


    /**
     * 409 CONFLICT
     */
    @ExceptionHandler({
        UserConflictException.class
    })
    public ResponseEntity<ApiResponse> ConflictException(BaseException exception) {
        return ResponseEntity.status(CONFLICT).body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }


    /**
     * 500 INTERNAL SERVER ERROR
     */

}
