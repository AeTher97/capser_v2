package com.mwozniak.capser_v2.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserDto {

    private String password;
    private String repeatPassword;
    private String username;
    private String email;
}
