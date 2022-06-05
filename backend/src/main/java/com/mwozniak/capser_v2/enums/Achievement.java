package com.mwozniak.capser_v2.enums;

import lombok.Getter;

public enum Achievement {

    PLACE_IN_EASY("Get placement in easy caps!"),
    PLACE_IN_SINGLES("Get placement in singles!"),

    PLAY_FIRST_GAME("Play first game!"),
    WIN_FIRST_GAME("Win first game!"),

    FIRST_NAKED_LAP("First naked lap!"),


    REBUTTALS_IN_A_ROW_5("5 rebuttals in a row!"),
    REBUTTALS_IN_A_ROW_7("7 rebuttals in a row!"),
    REBUTTALS_IN_A_ROW_10("10 rebuttals in a row!"),
    REBUTTALS_IN_A_ROW_12("12 rebuttals in a row!"),
    REBUTTALS_IN_A_ROW_15("15 rebuttals in a row!"),
    REBUT_ON_LAST_CHANCE("Rebut on your last chance!");


    @Getter
    private final String name;

    Achievement(String name) {
        this.name = name;
    }

}
