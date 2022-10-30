package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.validation.annotation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordDTO {

    private String oldPassword;
    @ValidPassword
    @NotNull
    private String newPassword;
}
