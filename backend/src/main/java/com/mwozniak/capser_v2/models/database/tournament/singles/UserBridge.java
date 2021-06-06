package com.mwozniak.capser_v2.models.database.tournament.singles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwozniak.capser_v2.models.database.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserBridge {

    public UserBridge(User user){
        this.user = user;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Getter
    @JsonIgnore
    private UUID id;

    @OneToOne
    @JsonIgnoreProperties(value = {"userSinglesStats","userEasyStats","userUnrankedStats","userDoublesStats","teams","lastSeen","lastGame","role"})
    private User user;
}
