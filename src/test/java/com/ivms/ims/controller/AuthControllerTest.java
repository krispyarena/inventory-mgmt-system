package com.ivms.ims.controller;

import com.ivms.ims.config.JwtService;
import com.ivms.ims.dto.UserDTO;
import com.ivms.ims.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    private UserDTO.RegistrationDTO registrationDTO;
    private UserDTO.LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        registrationDTO = new UserDTO.RegistrationDTO();
        registrationDTO.setUserName("testuser");
        registrationDTO.setPassword("password");

        loginDTO = new UserDTO.LoginDTO();
        loginDTO.setUserName("testuser");
        loginDTO.setPassword("password");
    }

    @Test
    void register_ShouldReturnCreatedUser() throws Exception {
        UserDTO createdUser = new UserDTO();
        createdUser.setUserName("testuser");

        when(userService.createUser(any(UserDTO.RegistrationDTO.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testuser\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"userName\":\"testuser\"}"));

        verify(userService, times(1)).createUser(any(UserDTO.RegistrationDTO.class));
    }

    @Test
    void login_ShouldReturnJwtToken() throws Exception {
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("test-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testuser\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("test-jwt-token"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
