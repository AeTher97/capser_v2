package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.validation.annotation.ValidPassword;
import lombok.Data;

@Data
public class UpdatePasswordDto {

    @ValidPassword
    private String password;
    private String code;
}
