package com.swm.cbz.common.exception;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException exception) {
            setErrorResponse(response, exception.getError());
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            ErrorMessage errorCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatusCode());
        response.setContentType(APPLICATION_JSON_VALUE);
        ApiResponse baseResponse = ApiResponse.error(errorCode);
        try {
            response.getWriter()
                    .write(objectMapper.configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true)
                            .writeValueAsString(baseResponse));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
