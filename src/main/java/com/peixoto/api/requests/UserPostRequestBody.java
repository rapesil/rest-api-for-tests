package com.peixoto.api.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserPostRequestBody {

    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @NotNull(message = "Password cannot be  null")
    private String password;

    @NotEmpty(message = "Role cannot be empty")
    @NotNull(message = "Role cannot be null")
    private String profile;
}
