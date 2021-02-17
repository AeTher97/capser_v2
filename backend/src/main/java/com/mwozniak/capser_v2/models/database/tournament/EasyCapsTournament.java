package com.mwozniak.capser_v2.models.database.tournament;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
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
public class EasyCapsTournament extends AbstractSinglesTournament<EasyCapsGame> {


    public EasyCapsTournament() {

    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<EasyCapsBracketEntry> bracketEntries;

    public List<? extends AbstractSinglesBracketEntry> getBracketEntries(){
        return bracketEntries;
    }

    @Override
    public GameType getGameType() {
        return GameType.EASY_CAPS;
    }


    @Override
    protected void setBracketEntries(List<AbstractSinglesBracketEntry> entries) {
        bracketEntries = (List<EasyCapsBracketEntry>) (List<?>) entries;
    }

    @Override
    protected AbstractSinglesBracketEntry createBracketEntry() {
        return new EasyCapsBracketEntry();
    }
}
