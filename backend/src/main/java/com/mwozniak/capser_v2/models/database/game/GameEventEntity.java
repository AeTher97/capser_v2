package com.mwozniak.capser_v2.models.database.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.enums.GameEvent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
public class GameEventEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @JsonIgnore
    private UUID id;

    @Setter
    @Enumerated(EnumType.STRING)
    private GameEvent gameEvent;

    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private Date time;

    @Setter
    private UUID userId;


}
