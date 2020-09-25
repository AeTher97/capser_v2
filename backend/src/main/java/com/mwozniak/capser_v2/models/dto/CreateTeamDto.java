package com.mwozniak.capser_v2.models.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class CreateTeamDto {

    @NotNull
    private List<UUID> players;
    @NotBlank
    private String name;
}
