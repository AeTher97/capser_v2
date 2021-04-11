package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.tournament.EasyCapsTournament;
import com.mwozniak.capser_v2.models.database.tournament.SinglesTournament;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.database.tournament.UnrankedTournament;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@Log4j
public class AggregatedTournamentController {

    private final EasyCapsTournamentController easyCapsTournamentController;
    private final SinglesTournamentController singlesTournamentController;
    private final UnrankedTournamentController unrankedTournamentController;

    public AggregatedTournamentController(EasyCapsTournamentController easyCapsTournamentController, SinglesTournamentController singlesTournamentController, UnrankedTournamentController unrankedTournamentController) {
        this.easyCapsTournamentController = easyCapsTournamentController;
        this.singlesTournamentController = singlesTournamentController;
        this.unrankedTournamentController = unrankedTournamentController;
    }

    @GetMapping
    public ResponseEntity<List<Tournament<?>>> getTournaments(@RequestParam int pageSize, @RequestParam int pageNumber) {
        log.info("Getting aggregated tournament list");
        List<EasyCapsTournament> easyCapsTournaments = easyCapsTournamentController.getTournaments(pageSize,pageNumber).getBody().getContent();
        List<SinglesTournament> singlesTournaments = singlesTournamentController.getTournaments(pageSize,pageNumber).getBody().getContent();
        List<UnrankedTournament> unrankedTournaments = unrankedTournamentController.getTournaments(pageSize,pageNumber).getBody().getContent();

        List<Tournament<?>> aggregatedList = new ArrayList<>();
        aggregatedList.addAll(easyCapsTournaments);
        aggregatedList.addAll(singlesTournaments);
        aggregatedList.addAll(unrankedTournaments);


        aggregatedList.sort(Tournament.Comparators.DATE);
        Collections.reverse(aggregatedList);
        if (aggregatedList.size() > 10) {
            return ResponseEntity.ok().body(aggregatedList.subList(0, 9));
        } else {
            return ResponseEntity.ok().body(aggregatedList);
        }
    }
}
