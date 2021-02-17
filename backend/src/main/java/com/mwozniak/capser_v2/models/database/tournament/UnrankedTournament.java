package com.mwozniak.capser_v2.models.database.tournament;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
public class UnrankedTournament extends AbstractSinglesTournament<UnrankedGame> {

    public UnrankedTournament() {

    }
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<UnrankedBracketEntry> bracketEntries;

    public List<? extends AbstractSinglesBracketEntry> getBracketEntries(){
        return bracketEntries;
    }

    @Override
    public GameType getGameType() {
        return GameType.UNRANKED;
    }

    @Override
    protected void setBracketEntries(List<AbstractSinglesBracketEntry> entries) {
        bracketEntries = (List<UnrankedBracketEntry>) (List<?>) entries;
    }

    @Override
    protected AbstractSinglesBracketEntry createBracketEntry() {
        return new UnrankedBracketEntry();
    }

}
