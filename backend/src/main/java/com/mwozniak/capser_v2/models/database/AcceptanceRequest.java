package com.mwozniak.capser_v2.models.database;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


@Table(name = "acceptance_requests")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcceptanceRequest {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")

    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private AcceptanceRequestType acceptanceRequestType;

    @Setter
    private UUID acceptingUser;

    @Setter
    private UUID gameToAccept;

    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private Date timestamp;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

    @Setter
    private UUID acceptingTeam;

    public static AcceptanceRequest createAcceptanceRequest(AcceptanceRequestType acceptanceRequestType, UUID acceptingUser, UUID gameToAccept, GameType gameType, UUID acceptingTeam){
        return AcceptanceRequest.builder()
                .acceptanceRequestType(acceptanceRequestType)
                .acceptingUser(acceptingUser)
                .gameToAccept(gameToAccept)
                .timestamp(new Date())
                .gameType(gameType)
                .acceptingTeam(acceptingTeam)
                .build();
    }

    public static AcceptanceRequest createAcceptanceRequest(AcceptanceRequestType acceptanceRequestType, UUID acceptingUser, UUID gameToAccept, GameType gameType){
       return createAcceptanceRequest(acceptanceRequestType,acceptingUser,gameToAccept,gameType,null);
    }
}
