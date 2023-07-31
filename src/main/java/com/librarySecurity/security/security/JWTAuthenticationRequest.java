package com.librarySecurity.security.security;

import lombok.Data;

@Data
public class JWTAuthenticationRequest {
    private String userName;
    private String password;
}
