package com.essam.pps.auth;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
}
