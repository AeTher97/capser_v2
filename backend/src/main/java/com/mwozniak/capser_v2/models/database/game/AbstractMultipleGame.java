package com.mwozniak.capser_v2.models.database.game;

import javax.persistence.MappedSuperclass;
import java.util.List;

@MappedSuperclass
public abstract class AbstractMultipleGame  extends AbstractGame {

    public abstract List<GamePlayerStats> getTeam1();

    public abstract List<GamePlayerStats> getTeam2();

    public abstract List<GamePlayerStats> getWinner();
}
