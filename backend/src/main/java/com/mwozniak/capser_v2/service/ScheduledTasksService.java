package com.mwozniak.capser_v2.service;


import com.mwozniak.capser_v2.service.game.EasyCapsGameService;
import com.mwozniak.capser_v2.service.game.SinglesGameService;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class ScheduledTasksService {

    private final DoublesService doublesService;
    private final SinglesGameService singlesGameService;
    private final EasyCapsGameService easyCapsGameService;
    private final UnrankedGameService unrankedGameService;

    public ScheduledTasksService(DoublesService doublesService, SinglesGameService singlesGameService, EasyCapsGameService easyCapsGameService, UnrankedGameService unrankedGameService) {
        this.doublesService = doublesService;
        this.singlesGameService = singlesGameService;
        this.easyCapsGameService = easyCapsGameService;
        this.unrankedGameService = unrankedGameService;
    }

    @Scheduled(fixedRate = 1800000, initialDelay = 10000)
    public void scheduleFixedRateTask() {
        log.info("Starting game accepting scan task");
        int totalAcceptedNumber = 0;
        totalAcceptedNumber += doublesService.acceptOverdueGames();
        totalAcceptedNumber += singlesGameService.acceptOverdueGames();
        totalAcceptedNumber += easyCapsGameService.acceptOverdueGames();
        totalAcceptedNumber += unrankedGameService.acceptOverdueGames();
        log.info("Automatically accepted total of "+ totalAcceptedNumber + " games.");
    }


}
