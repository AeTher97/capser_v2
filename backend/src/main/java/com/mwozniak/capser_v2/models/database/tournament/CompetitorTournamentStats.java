package com.mwozniak.capser_v2.models.database.tournament;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    public int wins;

    @Setter
    public int losses;

    @Setter
    public int points;

    @Setter
    public int pointsLost;

    @Setter
    public int sinks;

    @Setter
    public int sinksLost;

    public CompetitorTournamentStats() {
        //public constructor
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
