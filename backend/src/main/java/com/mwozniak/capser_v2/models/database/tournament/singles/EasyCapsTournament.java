package com.mwozniak.capser_v2.models.database.tournament.singles;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
public class EasyCapsTournament extends AbstractSinglesTournament {


    public EasyCapsTournament() {
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<EasyCapsBracketEntry> bracketEntries;

    @Override
    public List<BracketEntry> getBracketEntries(){
        return new ArrayList<>(bracketEntries);
    }


    @Override
    public GameType getGameType() {
        return GameType.EASY_CAPS;
    }


    @Override
    public void setBracketEntries(List<BracketEntry> entries) {
        bracketEntries = (List<EasyCapsBracketEntry>) (List<?>) entries;
    }

    @Override
    public AbstractSinglesBracketEntry createBracketEntry() {
        return new EasyCapsBracketEntry();
    }
}
