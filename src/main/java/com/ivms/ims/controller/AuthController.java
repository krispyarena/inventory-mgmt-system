package com.ivms.ims.controller;

import com.ivms.ims.config.JwtService;
import com.ivms.ims.dto.UserDTO;
import com.ivms.ims.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "Endpoints for managing user authentication")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @Operation(summary = "Register a User", description = "Registering a User into the system")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO.RegistrationDTO registrationDTO) {
        UserDTO createdUser = userService.createUser(registrationDTO);
        return ResponseEntity.ok(createdUser);
    }


    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authentication for users")
    public ResponseEntity<String> login(@RequestBody UserDTO.LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(jwtToken);
    }
}
