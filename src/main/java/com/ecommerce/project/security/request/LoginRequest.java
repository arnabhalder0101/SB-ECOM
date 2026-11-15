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
    @Size(min=3, max= 15, message = "username must be between 3 to 10 char")
    private String username;

    @NotBlank(message = "password must not be blank")
    @Size(min=5, message = "password must be of min 5 char")
    private String password;
}
