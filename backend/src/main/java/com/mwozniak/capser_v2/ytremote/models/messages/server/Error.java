package com.mwozniak.capser_v2.ytremote.models.messages.server;

import com.mwozniak.capser_v2.ytremote.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Error {

    private MessageType messageType = MessageType.ERROR;
    private String description;

    public Error(String description) {
        this.description = description;
    }
}
