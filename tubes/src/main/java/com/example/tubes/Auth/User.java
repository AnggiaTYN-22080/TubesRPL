package com.example.tubes.Auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String idUser;
    private String name;
    private String email;
    private String password;
    private String role;
}