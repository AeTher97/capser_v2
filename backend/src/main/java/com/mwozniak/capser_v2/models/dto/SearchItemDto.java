package com.mwozniak.capser_v2.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SearchItemDto {

    private UUID id;
    private String name;
    private SearchItemType type;

    public enum SearchItemType {
        TEAM, PLAYER
    }

}
