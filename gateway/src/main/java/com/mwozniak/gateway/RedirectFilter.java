package com.mwozniak.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Configuration
public class RedirectFilter {

    @Bean
    public WebFilter httpsRedirectFilter() {
        return (exchange, chain) -> {
            URI originalUri = exchange.getRequest().getURI();

            //here set your condition to http->https redirect
            List<String> forwardedValues = exchange.getRequest().getHeaders().get("x-forwarded-proto");
            if (forwardedValues != null && forwardedValues.contains("http")) {
                try {
                    URI mutatedUri = new URI("https",
                            originalUri.getUserInfo(),
                            originalUri.getHost(),
                            originalUri.getPort(),
                            originalUri.getPath(),
                            originalUri.getQuery(),
                            originalUri.getFragment());
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                    response.getHeaders().setLocation(mutatedUri);
                    return Mono.empty();
                } catch (URISyntaxException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
            return chain.filter(exchange);
        };
    }
}
