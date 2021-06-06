package com.mwozniak.capser_v2.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.database.User;
import lombok.Data;

import java.util.List;

@Data
public class TeamWithPlayersDto {

    private TeamWithStats teamWithStats;

    @JsonIgnoreProperties(value = {"userSinglesStats","teams","role","userEasyStats","userUnrankedStats"})
    private List<User> players;
}
