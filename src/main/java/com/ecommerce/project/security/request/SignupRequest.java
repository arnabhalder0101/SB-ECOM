package com.ecommerce.project.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "username must not be blank")
    @Size(min=3, max= 10, message = "username must be between 3 to 10 char")
    private String username;

    @Email
    @NotBlank(message = "email must not be blank")
    @Size(min=5, message = "email must be of min 5 char")
    private String email;

    @NotBlank(message = "password must not be blank")
    @Size(min=5, message = "password must be of min 5 char")
    private String password;

    private Set<String> role;

}
