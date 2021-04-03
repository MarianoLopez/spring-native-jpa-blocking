package com.z.nativejpablocking.error.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Builder
public class ErrorResponse {
    private final String message;
    private final Object payload;

    public static ErrorResponse from(Exception exception) {
        return new ErrorResponse(exception.getClass().getSimpleName(), exception.getLocalizedMessage());
    }
}
