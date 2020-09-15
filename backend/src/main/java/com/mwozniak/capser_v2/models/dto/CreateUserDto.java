package com.mwozniak.capser_v2.models.dto;

import lombok.Data;

@Data
public class CreateUserDto {

    private String password;
    private String repeatPassword;
    private String username;
}
