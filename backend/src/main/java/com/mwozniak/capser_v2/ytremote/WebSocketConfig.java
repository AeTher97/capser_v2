package com.mwozniak.capser_v2.ytremote;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final YouTubeHandler youTubeHandler;

    public WebSocketConfig(YouTubeHandler youTubeHandler) {
        this.youTubeHandler = youTubeHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(youTubeHandler, "/remote").setAllowedOrigins("*");
    }
}
