package com.mwozniak.capser_v2.models.database.tournament;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SinglesTournament extends AbstractSinglesTournament<SinglesGame> {


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<SinglesBracketEntry> bracketEntries;

    public List<? extends AbstractSinglesBracketEntry> getBracketEntries(){
        return bracketEntries;
    }


    @Override
    public GameType getGameType() {
        return GameType.SINGLES;
    }

    @Override
    protected void setBracketEntries(List<AbstractSinglesBracketEntry> entries) {
        bracketEntries = (List<SinglesBracketEntry>) (List<?>) entries;
    }

    @Override
    protected AbstractSinglesBracketEntry createBracketEntry() {
        return new SinglesBracketEntry();
    }


}
