package com.mwozniak.capser_v2.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mwozniak.capser_v2.enums.Roles;
import com.mwozniak.capser_v2.models.database.AchievementEntity;
import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.database.UserStats;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class UserDto {


    private UUID id;
    private String username;
    @JsonIgnore
    private String email;
    private Date lastSeen;
    private String avatarHash;
    private Date lastGame;
    private List<TeamWithStats> teams;

    private Roles role;

    private UserStats userSinglesStats;

    private UserStats userEasyStats;
    private UserStats userUnrankedStats;

    private UserStats userDoublesStats;
    @JsonProperty("achievements")
    private List<AchievementEntity> achievementEntities;
}
