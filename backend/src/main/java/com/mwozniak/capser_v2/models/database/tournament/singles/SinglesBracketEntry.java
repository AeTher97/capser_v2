package com.mwozniak.capser_v2.models.database.tournament.singles;

import com.mwozniak.capser_v2.models.database.game.single.SoloGame;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
public class SinglesBracketEntry extends AbstractSinglesBracketEntry {

    @OneToOne(cascade = CascadeType.PERSIST)
    @Setter
    private SoloGame game;



}
