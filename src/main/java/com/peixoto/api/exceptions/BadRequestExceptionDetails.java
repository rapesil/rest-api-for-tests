package com.peixoto.api.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;

import java.time.LocalDateTime;

@Data
@Builder
@Generated
public class BadRequestExceptionDetails {

    private String title;

    private int status;

    private String details;

    private LocalDateTime timestamp;
}
