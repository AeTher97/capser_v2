package com.mwozniak.capser_v2.models.responses;

import lombok.Data;

import java.util.Date;

@Data
public class UserMinimized {
    private String username;
    private String id;
    private Date lastSeen;
}
