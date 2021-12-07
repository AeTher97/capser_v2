package com.mwozniak.capser_v2.ytremote.models.messages.client;

import com.mwozniak.capser_v2.ytremote.enums.MessageType;
import lombok.Data;

@Data
public class SetReceiver {

    MessageType messageType;
    String deviceName;

}
