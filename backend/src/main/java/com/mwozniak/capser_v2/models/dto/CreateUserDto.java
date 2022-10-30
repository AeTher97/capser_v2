package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.validation.annotation.ValidPassword;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserDto {

    @ValidPassword
    private String password;
    @ValidPassword
    private String repeatPassword;
    private String username;
    private String email;
}
