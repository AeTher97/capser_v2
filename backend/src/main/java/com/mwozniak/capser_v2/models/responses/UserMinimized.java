package com.mwozniak.capser_v2.models.responses;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserMinimized {
    private String username;
    private UUID id;
    private Date lastSeen;
    private String avatarHash;
}
