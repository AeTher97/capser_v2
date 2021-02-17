package com.mwozniak.capser_v2.enums;

import lombok.Getter;

import java.util.Arrays;

public enum BracketEntryType {
    RO_64(64),RO_32(32), RO_16(16),RO_8(8),RO_4(4),RO_2(2);

    @Getter
    private final int value;

    BracketEntryType(int i) {
        value = i;
    }

    public static BracketEntryType getHigher(BracketEntryType entryType){
        if(entryType.equals(RO_2)){
            return RO_2;
        }
        return Arrays.stream(BracketEntryType.values()).filter(bracketEntryType -> bracketEntryType.getValue()==entryType.getValue()/2).findFirst().get();
    }

    public static BracketEntryType getLower(BracketEntryType entryType){
        if(entryType.equals(RO_64)){
            return RO_64;
        }
        return Arrays.stream(BracketEntryType.values()).filter(bracketEntryType -> bracketEntryType.getValue()==entryType.getValue()*2).findFirst().get();
    }
}

