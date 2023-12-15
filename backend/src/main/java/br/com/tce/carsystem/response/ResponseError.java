package br.com.tce.carsystem.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseError {

    private String message;
    private Integer errorCode;
}
