package com.mwozniak.capser_v2.models.database.tournament;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Comparator;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Entity
@Builder
public class CompetitorTournamentStats {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @JsonIgnore
    protected UUID id;

    @Getter
    private UUID competitorId;

    @Setter
    private int wins;

    @Setter
    private int losses;

    @Setter
    private int points;

    @Setter
    private int pointsLost;

    @Setter
    private int sinks;

    @Setter
    private int sinksLost;

    public CompetitorTournamentStats() {

    }


    public static class Comparators {

        public static final Comparator<CompetitorTournamentStats> DRAW_RESOLVE_CONDITIONS = (o1, o2) -> {
            if(o1.wins>o2.wins){
                return -1;
            } else if (o1.wins < o2.wins){
                return 1;
            }
            if(o1.losses<o2.losses){
                return -1;
            } else if(o1.losses > o2.losses){
                return 1;
            }
            if(o1.points>o2.points){
                return -1;
            } else if (o1.points < o2.points){
                return 1;
            }
            if(o1.pointsLost<o2.pointsLost){
                return -1;
            } else if (o1.pointsLost > o2.pointsLost){
                return 1;
            }
            if(o1.sinks>o2.sinks){
                return -1;
            } else if (o1.sinks < o2.sinks){
                return 1;
            }
            if(o1.sinksLost<o2.sinksLost){
                return -1;
            } else if (o1.sinksLost > o2.sinksLost){
                return 1;
            }
            return 0;
        };
    }

}
