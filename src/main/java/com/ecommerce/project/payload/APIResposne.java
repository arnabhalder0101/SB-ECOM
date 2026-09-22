package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResposne {

    private  String message;
    private Object object;
    private boolean status;

}

/*

Generalized response with message.

status:
message:
Object:


 */