package com.mwozniak.capser_v2.ytremote.models.messages.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwozniak.capser_v2.ytremote.enums.MemberType;
import com.mwozniak.capser_v2.ytremote.enums.MessageType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Start {

    private MessageType messageType;
    private String deviceName;
    private MemberType memberType;
}
