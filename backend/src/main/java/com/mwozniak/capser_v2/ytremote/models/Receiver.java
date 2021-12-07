package com.mwozniak.capser_v2.ytremote.models;

import lombok.Data;

import java.util.UUID;

@Data
public class Receiver {
    private String deviceName;
    private UUID uuid;
}
