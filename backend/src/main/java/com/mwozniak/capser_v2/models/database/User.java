package com.mwozniak.capser_v2.models.database;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")

    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Setter
    private String username;

    @JsonIgnore
    @Setter
    private String password;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSeen;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_single_stats", referencedColumnName = "id", nullable = false)
    private UserStats userSinglesStats;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_easy_stats", referencedColumnName = "id", nullable = false)
    private UserStats userEasyStats;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_unranked_stats", referencedColumnName = "id", nullable = false)
    private UserStats userUnrankedStats;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_doubles_stats", referencedColumnName = "id", nullable = false)
    private UserStats userDoublesStats;
}
