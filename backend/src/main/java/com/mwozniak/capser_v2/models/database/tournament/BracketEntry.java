package com.mwozniak.capser_v2.models.database.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Comparator;
import java.util.UUID;

@AllArgsConstructor
@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class BracketEntry {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Getter
    private UUID id;

    @Setter
    private int coordinate;

    @Setter
    private boolean isBye;

    @Setter
    private boolean isFinal;

    @Setter
    private BracketEntryType bracketEntryType;

    public static class Comparators {

        public static final Comparator<BracketEntry> COORDINATE = Comparator.comparing(BracketEntry::getCoordinate);
    }


}
