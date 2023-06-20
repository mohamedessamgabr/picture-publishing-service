package com.essam.pps.auth;


import com.essam.pps.entity.AppUser;
import com.essam.pps.entity.Role;
import com.essam.pps.security.jwt.JwtService;
import com.essam.pps.service.RoleService;
import com.essam.pps.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleService roleService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        Role role = roleService.findByName("USER");
        AppUser user = AppUser
                .builder()
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .build();
        user.addRole(role);
        userService.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword())
        );
        AppUser user = userService.findByEmail(authenticationRequest.getEmail());
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

}
