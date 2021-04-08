package com.mwozniak.capser_v2.cron;

import com.mwozniak.capser_v2.models.database.TimeSeries;
import com.mwozniak.capser_v2.models.database.UserStats;
import com.mwozniak.capser_v2.repository.StatsRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class TimeSeriesCron {

    private final StatsRepository statsRepository;


    public TimeSeriesCron(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public static TimeSeries createEmptyTimeSeries() {
        TimeSeries timeSeries = new TimeSeries();
        float NULL_VALUE = -100000;
        float[] emptyList = new float[365];
        for (int i = 0; i < 365; i++) {
            emptyList[i] = NULL_VALUE;
        }
        timeSeries.setData(emptyList);
        return timeSeries;
    }

    @Scheduled(cron = "0 0 23 * * *")
    public void updateTimeSeries() {
        log.info("Running time series cron");
        createPointsCron();
    }

    private void createPointsCron() {
        List<UserStats> statsList = statsRepository.findAll();
        for (UserStats stats : statsList) {
            logTodayPoints(stats);
        }
        statsRepository.saveAll(statsList);
    }

    private void logTodayPoints(UserStats userStats) {
        if (userStats.getPointsSeries() == null) {
            userStats.setPointsSeries(createEmptyTimeSeries());
        }
        if (userStats.getRebuttalsSeries() == null) {
            userStats.setRebuttalsSeries(createEmptyTimeSeries());
        }
        userStats.getRebuttalsSeries().logToday(userStats.getAvgRebuttals());
        userStats.getPointsSeries().logToday(userStats.getPoints());
    }
}
