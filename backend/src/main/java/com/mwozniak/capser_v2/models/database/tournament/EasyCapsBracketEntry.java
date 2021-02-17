package com.mwozniak.capser_v2.models.database.tournament;

import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
public class EasyCapsBracketEntry extends AbstractSinglesBracketEntry{

    @OneToOne(cascade = CascadeType.PERSIST)
    @Setter
    private EasyCapsGame game;


}
