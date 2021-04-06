package com.mwozniak.capser_v2.enums;

import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum BracketEntryType {
    RO_64(64),RO_32(32), RO_16(16),RO_8(8),RO_4(4),RO_2(2),
    D_RO_1(202),D_RO_2(204),D_RO_3(206),D_RO_4(208),D_RO_6(212),D_RO_8(216),D_RO_12(224),D_RO_16(236);

    @Getter
    private final int value;

    BracketEntryType(int i) {
        value = i;
    }

    public static BracketEntryType getHigher(BracketEntryType entryType){
        if(entryType.name().contains("D_RO")){
            if (entryType.equals(D_RO_1)) {
                return D_RO_1;
            }
            List<BracketEntryType> entryTypeList = Arrays.stream(BracketEntryType.values()).filter(bracketEntryType -> bracketEntryType.name().contains("D_RO")).collect(Collectors.toList());
            entryTypeList.sort(Comparators.VALUE_DESCENDING);
            for(BracketEntryType entryType1 : entryTypeList){
                if(entryType1.getValue()<entryType.getValue()){
                    return entryType1;
                }
            }
            return null;
        } else {
            if (entryType.equals(RO_2)) {
                return RO_2;
            }
            return Arrays.stream(BracketEntryType.values()).filter(bracketEntryType -> !bracketEntryType.name().contains("D_RO")).filter(bracketEntryType -> bracketEntryType.getValue() == entryType.getValue() / 2).findFirst().get();
        }
    }

    public static BracketEntryType getLower(BracketEntryType entryType){
        if(entryType.name().contains("D_RO")){
            if (entryType.equals(D_RO_16)) {
                return D_RO_16;
            }
            List<BracketEntryType> entryTypeList = Arrays.stream(BracketEntryType.values()).filter(bracketEntryType -> bracketEntryType.name().contains("D_RO")).collect(Collectors.toList());
            entryTypeList.sort(Comparators.VALUE);
            for(BracketEntryType entryType1 : entryTypeList){
                if(entryType1.getValue()>entryType.getValue()){
                    return entryType1;
                }
            }
            return null;
        } else {
            if (entryType.equals(RO_64)) {
                return RO_64;
            }
            return Arrays.stream(BracketEntryType.values()).filter(bracketEntryType -> !bracketEntryType.name().contains("D_RO")).filter(bracketEntryType -> bracketEntryType.getValue() == entryType.getValue() * 2).findFirst().get();
        }
    }

    public static boolean isPowerOf2(BracketEntryType entryType){
        int n = Integer.parseInt(entryType.name().split("_")[entryType.name().split("_").length-1]);
        return (n & n-1)==0;
    }

    public static BracketEntryType getHigherPowerOf2(BracketEntryType entryType){
        if(isPowerOf2(entryType) && entryType.name().contains("D_RO")){
            return getHigher(getHigher(entryType));
        } else if(!isPowerOf2(entryType) && entryType.name().contains("D_RO")){
            return getHigher(entryType);
        } else {
            return getHigher(entryType);
        }
    }

    public static int getSingleEliminationCountAbove(BracketEntryType bracketEntryType){
        int number =0;
        for(BracketEntryType entry : BracketEntryType.values()){
            if(entry.getValue() < bracketEntryType.getValue()){
                number += entry.getValue()/2;
            }
        }
        return number;
    }

    public static int getSingleEliminationCountAboveAndEqual(BracketEntryType bracketEntryType){
        int number =0;
        for(BracketEntryType entry : BracketEntryType.values()){
            if(entry.getValue() <= bracketEntryType.getValue()){
                number += entry.getValue()/2;
            }
        }
        return number;
    }

    public static int getDoubleEliminationCountInRow(BracketEntryType bracketEntryType,boolean upper){
        return getDoubleEliminationCountAboveAndEqual(bracketEntryType,upper) - getDoubleEliminationCountAbove(bracketEntryType,upper);
    }

    public static int getDoubleEliminationCountAbove(BracketEntryType bracketEntryType, boolean upper){
        int number =0;
        if(upper){
            switch (bracketEntryType){
                case D_RO_16:
                case D_RO_12:
                    return getSingleEliminationCountAbove(RO_16)+1;
                case D_RO_8:
                    return getSingleEliminationCountAbove(RO_8)+1;
                case D_RO_4:
                    return getSingleEliminationCountAbove(RO_4)+1;
                case D_RO_2:
                    return getSingleEliminationCountAbove(RO_2)+1;
            }
        } else {
            switch (bracketEntryType){
                case D_RO_2:
                    return 0;
                case D_RO_3:
                    return 1;
                case D_RO_4:
                    return 2;
                case D_RO_6:
                    return 4;
                case D_RO_8:
                    return 6;
                case D_RO_12:
                    return 10;
                case D_RO_16:
                    return 14;
            }
            return number;
        }
        return 0;
    }

    public static int getDoubleEliminationCountAboveAndEqual(BracketEntryType bracketEntryType, boolean upper){
        if(upper){
            switch (bracketEntryType){
                case D_RO_16:
                    return getSingleEliminationCountAboveAndEqual(RO_16)+1;
                case D_RO_8:
                    return getSingleEliminationCountAboveAndEqual(RO_8)+1;
                case D_RO_4:
                    return getSingleEliminationCountAboveAndEqual(RO_4)+1;
                case D_RO_2:
                    return getSingleEliminationCountAboveAndEqual(RO_2)+1;
            }
        } else {
            switch (bracketEntryType){
                case D_RO_2:
                    return 1;
                case D_RO_3:
                    return 2;
                case D_RO_4:
                    return 4;
                case D_RO_6:
                    return 6;
                case D_RO_8:
                    return 10;
                case D_RO_12:
                case D_RO_16:
                    return 14;
            }
        }
        return 0;
    }



    public static class Comparators {

        public static final Comparator<BracketEntryType> VALUE = Comparator.comparing(BracketEntryType::getValue);
        public static final Comparator<BracketEntryType> VALUE_DESCENDING = Comparator.comparing(BracketEntryType::getValue).reversed();
    }

}

