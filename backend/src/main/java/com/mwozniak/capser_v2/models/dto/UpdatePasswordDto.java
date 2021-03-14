package com.mwozniak.capser_v2.models.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {

    private String password;
    private String code;
}
