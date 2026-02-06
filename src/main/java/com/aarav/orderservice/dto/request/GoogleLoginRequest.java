package com.aarav.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public class GoogleLoginRequest {
    @NotBlank
    private String idToken;

    public @NotBlank String getIdToken() {
        return idToken;
    }

    public void setIdToken(@NotBlank String idToken) {
        this.idToken = idToken;
    }
}
