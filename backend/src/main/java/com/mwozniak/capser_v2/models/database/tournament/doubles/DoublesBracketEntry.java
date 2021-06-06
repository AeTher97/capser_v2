package com.mwozniak.capser_v2.models.database.tournament.doubles;

import com.mwozniak.capser_v2.models.database.Competitor;
import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.database.game.multiple.AbstractMultipleGame;
import com.mwozniak.capser_v2.models.database.game.multiple.DoublesGame;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
public class DoublesBracketEntry extends BracketEntry {

    @OneToOne
    @Setter
    private TeamWithStats team1;

    @OneToOne
    @Setter
    private TeamWithStats team2;



    @OneToOne(cascade = CascadeType.PERSIST)
    @Setter
    private DoublesGame game;


    public AbstractMultipleGame getGame(){
        return game;
    }


    @Override
    public void setCompetitor1(Competitor competitor) {
        setTeam1((TeamWithStats) competitor);
    }

    @Override
    public void setCompetitor2(Competitor competitor) {
        setTeam2((TeamWithStats) competitor);
    }

    @Override
    public Competitor getCompetitor1() {
        return team1;
    }

    @Override
    public Competitor getCompetitor2() {
        return team2;
    }

}
