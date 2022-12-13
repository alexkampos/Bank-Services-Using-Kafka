package org.marlow.configuration;

import org.marlow.model.dto.AmountNotPositiveNumber;
import org.marlow.model.dto.ApiErrorResponse;
import org.marlow.model.dto.OverdraftException;
import org.marlow.util.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity handleUsernameNotFoundException(Exception ex, WebRequest request) {
        return prepareRestException(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OverdraftException.class)
    public ResponseEntity handleOverdraftException(Exception ex, WebRequest request) {
        return prepareRestException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AmountNotPositiveNumber.class)
    public ResponseEntity handleAmountNotPositiveNumber(Exception ex, WebRequest request) {
        return prepareRestException(ex, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gathers error info and constructs a JSON object that encapsulates those info.
     * @param ex The exception that occurred.
     * @param request The entity holding the path info.
     * @param status The http error status.
     * @return The JSON object holding the error info.
     */
    private ResponseEntity<Object> prepareRestException(Exception ex, WebRequest request, HttpStatus status) {
        logger.info(ex.getClass().getName());
        String error = ex.getLocalizedMessage();
        if (ex.getCause() != null) {
            error += String.format(". %s", ex.getCause().getMessage());
        } else if (ObjectUtils.isEmpty(error)) {
            error = Constants.ERROR_MESSAGE_NOT_AVAILABLE;
        }
        final ApiErrorResponse response = ApiErrorResponse.builder()
                .result(Constants.RESPONSE_NOK)
                .status(status.value())
                .path(getPath(request))
                .message(error)
                .exception(ex.getClass().getName())
                .build();
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}
