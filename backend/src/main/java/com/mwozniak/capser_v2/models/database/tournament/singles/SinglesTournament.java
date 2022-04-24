package com.mwozniak.capser_v2.models.database.tournament.singles;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.doubles.DoublesBracketEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SinglesTournament extends AbstractSinglesTournament<SinglesGame> {


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<SinglesBracketEntry> bracketEntries;

    public List< BracketEntry> getBracketEntries(){
        return  (List<BracketEntry>) (List<?>) bracketEntries;
    }


    @Override
    public GameType getGameType() {
        return GameType.SINGLES;
    }

    @Override
    public void setBracketEntries(List<BracketEntry> entries) {
        bracketEntries = (List<SinglesBracketEntry>) (List<?>) entries;
    }

    @Override
    public AbstractSinglesBracketEntry createBracketEntry() {
        return new SinglesBracketEntry();
    }


}
