package com.quan12yt.trackingcronjob.controller;

import com.quan12yt.trackingcronjob.dto.request.LoginRequest;
import com.quan12yt.trackingcronjob.dto.response.LoginResponse;
import com.quan12yt.trackingcronjob.config.jwt.CustomUserDetails;
import com.quan12yt.trackingcronjob.config.jwt.JWTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class JwtAuthController {
    @Autowired
    private JWTokenProvider jwTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (request.getUserName(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwTokenProvider.generateJWT
                ((CustomUserDetails) authentication.getPrincipal());
        List<GrantedAuthority> roles = (List<GrantedAuthority>) authentication.getAuthorities();
        return new ResponseEntity<>(new LoginResponse
                ("true", authentication.getName(), roles, token), HttpStatus.OK);
    }
}
