package com.mwozniak.capser_v2.ytremote.models.messages.server;

import com.mwozniak.capser_v2.ytremote.enums.MessageType;
import com.mwozniak.capser_v2.ytremote.models.Receiver;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Receivers {

    @Getter
    MessageType messageType;
    @Setter
    @Getter
    List<Receiver> receivers;

    public Receivers() {
        messageType = MessageType.RECEIVERS;
    }
}
