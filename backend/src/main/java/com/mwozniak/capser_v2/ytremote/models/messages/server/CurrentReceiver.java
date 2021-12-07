package com.mwozniak.capser_v2.ytremote.models.messages.server;

import com.mwozniak.capser_v2.ytremote.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class CurrentReceiver {

    private MessageType messageType = MessageType.CURRENT_RECEIVER;
    private String deviceName;

    public CurrentReceiver(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
