package com.mwozniak.capser_v2.security.providers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenHolder {

    private String authToken;
    private String refreshToken;
}
