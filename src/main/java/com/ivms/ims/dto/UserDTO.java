package com.ivms.ims.dto;

import com.ivms.ims.model.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String userName;
    private String email;
    private String role;

    @Data
    public static class RegistrationDTO {
        private String userName;
        private String email;
        private String password;
        private Role role;
    }

    @Data
    public static class LoginDTO {
        private String userName;
        private String password;
    }
}
