package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.enums.BracketEntryType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.enums.SeedType;
import com.mwozniak.capser_v2.enums.TournamentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTournamentDto {

    @NotNull
    private String tournamentName;
    @NotNull
    private TournamentType tournamentType;
    @NotNull
    private SeedType seedType;
    @NotNull
    private BracketEntryType size;
}
