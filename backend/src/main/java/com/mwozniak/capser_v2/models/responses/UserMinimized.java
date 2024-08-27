package com.mwozniak.capser_v2.models.responses;

import com.mwozniak.capser_v2.models.database.User;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserMinimized {
    private String username;
    private UUID id;
    private Date lastSeen;
    private String avatarHash;

    public static UserMinimized fromUser(User user){
        UserMinimized minimized = new UserMinimized();
        minimized.setId(user.getId());
        minimized.setUsername(user.getUsername());
        minimized.setLastSeen(user.getLastSeen());
        minimized.setAvatarHash(user.getAvatarHash());
        return minimized;
    }
}
