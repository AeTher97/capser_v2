package com.mwozniak.capser_v2.models.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.enums.Roles;
import com.mwozniak.capser_v2.models.dto.CreateUserDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "users")
public class User implements Competitor{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")

    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    public User() {
        userDoublesStats = new UserStats();
        userSinglesStats = new UserStats();
        userEasyStats = new UserStats();
        userUnrankedStats = new UserStats();
    }

    @Setter
    private String username;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "player_single_stats", referencedColumnName = "id", nullable = false)
    private final UserStats userSinglesStats;

    @JsonIgnore
    @Setter
    private String password;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSeen;

    @Setter
    @Temporal(TemporalType.DATE)
    private Date lastGame;

    @Setter
    @ElementCollection(fetch = FetchType.LAZY)
    private List<UUID> teams;

    @Setter
    @Enumerated(EnumType.STRING)
    private Roles role;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "player_easy_stats", referencedColumnName = "id", nullable = false)
    private final UserStats userEasyStats;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "player_unranked_stats", referencedColumnName = "id", nullable = false)
    private final UserStats userUnrankedStats;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "player_doubles_stats", referencedColumnName = "id", nullable = false)
    private final UserStats userDoublesStats;
    @JsonIgnore
    private String email;
    private String avatarHash;

    public static User createUserFromDto(CreateUserDto createUserDto, String encodedPassword) throws NoSuchAlgorithmException {
        User user = new User();
        user.setPassword(encodedPassword);
        user.setRole(Roles.USER);
        user.setUsername(createUserDto.getUsername());
        user.setTeams(new ArrayList<>());
        user.setEmail(createUserDto.getEmail());
        return user;
    }

    public void setEmail(String email) throws NoSuchAlgorithmException {
        this.email = email;
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(email.toLowerCase().trim().getBytes());
        byte[] array = messageDigest.digest();
        StringBuilder stringBuffer = new StringBuilder();
        for (byte b : array) {
            stringBuffer.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
        }
        avatarHash = stringBuffer.toString();
    }

    protected void setId(UUID id){
        this.id = id;
    }
}
