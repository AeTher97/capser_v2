package com.mwozniak.capser_v2.ytremote.models.messages.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwozniak.capser_v2.ytremote.enums.Action;
import lombok.Data;

import java.awt.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaControl {
    private TrayIcon.MessageType messageType;
    private Action action;
    private Integer timeSet;
}
