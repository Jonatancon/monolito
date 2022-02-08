package com.pragma.monolito.domain;

import lombok.*;

@Data
@Builder
public class Response {

    private int status;
    private String userMessage;
    private String developerMessage;
    private String errorCode;
    private String moreInfo;
}
