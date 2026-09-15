package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResposne {

    private HttpStatus statusCode;
    private  String message;
    private String object;

}

/*

Generalized response with message.

status:
message:
Object:


 */