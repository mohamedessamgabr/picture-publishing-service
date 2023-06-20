package com.essam.pps.auth;

import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class RegistrationRequest {
    @Email(message = "Invalid Email Address")
    private String email;
    private String password;
}
