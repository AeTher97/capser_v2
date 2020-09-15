package com.mwozniak.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	private static final String SEGMENT = "/${segment}";

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder){
		return routeLocatorBuilder.routes()
				.route("api", r-> r
						.path("/api/**")
						.filters(spec -> spec.rewritePath("/api/(?<segment>.*)", SEGMENT))
						.uri("lb://capser-backend"))
				.route("frontend", r-> r.path("/**").uri("lb://frontend"))
				.build();
	}

}
