package com.essam.pps.rest;

import com.essam.pps.auth.AuthenticationRequest;
import com.essam.pps.auth.AuthenticationResponse;
import com.essam.pps.auth.AuthenticationService;
import com.essam.pps.auth.RegistrationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/pps/authentication")
@RequiredArgsConstructor
@Api(tags = "Users' Registration and Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @ApiOperation(value = "Create a new user and get the JWT token")
    @PostMapping(value = "/register")
    @PreAuthorize("permitAll")
    public ResponseEntity<AuthenticationResponse> registerNewUser(
            @ApiParam(name="User Data", value = "The data of the user to be created") @RequestBody @Valid RegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @ApiOperation(value = "authenticate the user to get the JWT token")
    @PostMapping(value = "/authenticate")
    @PreAuthorize("permitAll")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @ApiParam(name="User Data", value = "The data of the user to be authenticated")
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


}
