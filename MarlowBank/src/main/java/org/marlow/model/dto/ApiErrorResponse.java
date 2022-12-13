package org.marlow.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ApiErrorResponse implements Serializable {

    private String result;

    private Integer status;

    private String path;

    private String message;

    private String exception;

}
