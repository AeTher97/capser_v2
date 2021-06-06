package com.mwozniak.capser_v2.models.database.tournament.doubles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.models.database.TeamWithStats;
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
public class TeamBridge {

    public TeamBridge(TeamWithStats team){
        this.team = team;
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
//    @JsonIgnoreProperties(value = {"userSinglesStats","userEasyStats","userUnrankedStats","userDoublesStats","teams","lastSeen","lastGame","role"})
    private TeamWithStats team;
}
