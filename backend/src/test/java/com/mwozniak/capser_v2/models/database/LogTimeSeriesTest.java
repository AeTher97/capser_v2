package com.mwozniak.capser_v2.models.database;


import com.mwozniak.capser_v2.cron.TimeSeriesCron;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Time Series Test")
public class LogTimeSeriesTest {

    @Test
    public void whenOneDayDifference_correctlyRecordsNewValue() {
        TimeSeries timeSeries = TimeSeriesCron.createEmptyTimeSeries();
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        timeSeries.setLastLogged(yesterdayDate);

        timeSeries.logToday(12);

        assertAll(
                () -> assertEquals(1, DAYS.between(yesterdayDate, LocalDate.now())),
                () -> assertEquals(LocalDate.now(), timeSeries.getLastLogged()),
                () -> assertEquals(12, timeSeries.getData()[1]),
                () -> assertEquals(1, timeSeries.getLastElement()));


    }

    @Test
    public void whenOneDayDifferenceEndOfTheYear_correctlyRecordsNewValue() {
        TimeSeries timeSeries = TimeSeriesCron.createEmptyTimeSeries();
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        timeSeries.setLastLogged(yesterdayDate);
        timeSeries.setLastElement(364);

        timeSeries.logToday(12);

        assertAll(
                () -> assertEquals(1, DAYS.between(yesterdayDate, LocalDate.now())),
                () -> assertEquals(LocalDate.now(), timeSeries.getLastLogged()),
                () -> assertEquals(12, timeSeries.getData()[0]),
                () -> assertEquals(0, timeSeries.getLastElement()));

    }

    @Test
    public void whenZeroDayDifference_correctlyOverwritesNewValue() {
        TimeSeries timeSeries = TimeSeriesCron.createEmptyTimeSeries();
        timeSeries.getData()[0] = 0.12f;
        LocalDate todayDate = LocalDate.now();
        timeSeries.setLastLogged(todayDate);

        assertEquals(0.12f, timeSeries.getData()[0]);

        timeSeries.logToday(12);

        assertAll(
                () -> assertEquals(0, DAYS.between(todayDate, LocalDate.now())),
                () -> assertEquals(LocalDate.now(), timeSeries.getLastLogged()),
                () -> assertEquals(12, timeSeries.getData()[0]),
                () -> assertEquals(0, timeSeries.getLastElement()));


    }

    @Test
    public void whenFiveDayDifference_correctlyResetsValuesInBetween() {
        TimeSeries timeSeries = TimeSeriesCron.createEmptyTimeSeries();
        timeSeries.getData()[10] = 0.12f;
        timeSeries.getData()[11] = 0.16f;
        timeSeries.getData()[12] = 0.11f;
        timeSeries.getData()[13] = 0.9f;
        timeSeries.getData()[14] = 0.02f;
        timeSeries.setLastElement(10);
        LocalDate fiveDaysAgo = LocalDate.now().minusDays(5);
        timeSeries.setLastLogged(fiveDaysAgo);

        assertEquals(0.12f, timeSeries.getData()[10]);
        assertEquals(0.16f, timeSeries.getData()[11]);
        assertEquals(0.11f, timeSeries.getData()[12]);
        assertEquals(0.9f, timeSeries.getData()[13]);
        assertEquals(0.02f, timeSeries.getData()[14]);

        timeSeries.logToday(12);

        assertAll(
                () -> assertEquals(5, DAYS.between(fiveDaysAgo, LocalDate.now())),
                () -> assertEquals(LocalDate.now(), timeSeries.getLastLogged()),
                () -> assertEquals(12, timeSeries.getData()[15]),
                () -> assertEquals(15, timeSeries.getLastElement()));

        assertEquals(-100000, timeSeries.getData()[0]);
        assertEquals(-100000, timeSeries.getData()[10]);
        assertEquals(-100000, timeSeries.getData()[11]);
        assertEquals(-100000, timeSeries.getData()[12]);
        assertEquals(-100000, timeSeries.getData()[13]);
        assertEquals(-100000, timeSeries.getData()[14]);

    }

    @Test
    public void whenSevenDayDifference_correctlyResetsValuesInBetweenEndOfYear() {
        TimeSeries timeSeries = TimeSeriesCron.createEmptyTimeSeries();
        timeSeries.getData()[361] = 0.12f;
        timeSeries.getData()[362] = 0.16f;
        timeSeries.getData()[363] = 0.11f;
        timeSeries.getData()[364] = 0.9f;
        timeSeries.getData()[0] = 0.02f;
        timeSeries.getData()[1] = 0.01f;
        timeSeries.getData()[2] = 0.03f;
        timeSeries.getData()[149] = 0.001f;

        timeSeries.setLastElement(361);
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        timeSeries.setLastLogged(sevenDaysAgo);

        assertEquals(-100000, timeSeries.getData()[360]);
        assertEquals(0.12f, timeSeries.getData()[361]);
        assertEquals(0.16f, timeSeries.getData()[362]);
        assertEquals(0.11f, timeSeries.getData()[363]);
        assertEquals(0.9f, timeSeries.getData()[364]);
        assertEquals(0.02f, timeSeries.getData()[0]);
        assertEquals(0.01f, timeSeries.getData()[1]);
        assertEquals(0.03f, timeSeries.getData()[2]);
        assertEquals(-100000, timeSeries.getData()[3]);

        timeSeries.logToday(12);

        assertAll(
                () -> assertEquals(7, DAYS.between(sevenDaysAgo, LocalDate.now())),
                () -> assertEquals(LocalDate.now(), timeSeries.getLastLogged()),
                () -> assertEquals(12, timeSeries.getData()[3]),
                () -> assertEquals(3, timeSeries.getLastElement()));

        assertEquals(0.001f, timeSeries.getData()[149]);
        assertEquals(-100000, timeSeries.getData()[150]);
        assertEquals(-100000, timeSeries.getData()[360]);
        assertEquals(-100000, timeSeries.getData()[361]);
        assertEquals(-100000, timeSeries.getData()[362]);
        assertEquals(-100000, timeSeries.getData()[363]);
        assertEquals(-100000, timeSeries.getData()[364]);
        assertEquals(-100000, timeSeries.getData()[0]);
        assertEquals(-100000, timeSeries.getData()[1]);
        assertEquals(-100000, timeSeries.getData()[2]);
        assertEquals(-100000, timeSeries.getData()[12]);

    }
}
