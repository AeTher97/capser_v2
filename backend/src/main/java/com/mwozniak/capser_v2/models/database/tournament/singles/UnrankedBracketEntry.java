package com.mwozniak.capser_v2.models.database.tournament.singles;

import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
public class UnrankedBracketEntry extends AbstractSinglesBracketEntry {

    @OneToOne(cascade = CascadeType.PERSIST)
    @Setter
    private UnrankedGame game;



}
