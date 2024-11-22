package com.ivms.ims.service;

import com.ivms.ims.dto.UserDTO;
import com.ivms.ims.model.Role;
import com.ivms.ims.model.User;
import com.ivms.ims.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ValidData_ReturnsUserDTO() {
        UserDTO.RegistrationDTO registrationDTO = new UserDTO.RegistrationDTO();
        registrationDTO.setUserName("testuser");
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setPassword("password");
        registrationDTO.setRole(Role.valueOf("CUSTOMER"));

        User mockUser = new User();
        mockUser.setId(1L);

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserDTO userDTO = userService.createUser(registrationDTO);

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserById_UserExists_ReturnsUserDTO() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        UserDTO userDTO = userService.getUserById(1L);

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
        assertEquals("testuser", userDTO.getUserName());
    }
}
