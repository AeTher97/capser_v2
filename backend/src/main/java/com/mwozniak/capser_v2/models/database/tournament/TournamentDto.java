package com.mwozniak.capser_v2.models.database.tournament;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDto {

    protected BracketEntryType size;
    protected SeedType seedType;
    protected String tournamentName;
    protected boolean isFinished;
    protected boolean isSeeded;
    private UUID id;
    private TournamentType tournamentType;
    private Date date;

    private GameType gameType;

}
