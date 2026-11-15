package com.ecommerce.project.security.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {

    @NotBlank(message = "username must not be blank")
    @Size(min=3, message = "username must be min 3 character")
    private String username;

    @NotBlank(message = "password must not be blank")
    @Size(min=5, message = "password must be of min 5 char")
    private String password;
}
