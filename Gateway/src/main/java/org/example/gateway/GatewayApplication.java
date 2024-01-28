package org.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


    //configure routes using RouteLocatorBuilder and RouteLocator

    @Bean
    public RouteLocator getRouteDefinitionLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(r -> r.path("/customers/**").uri("lb://CUSTOMER-SERVICE"))
                .route(r -> r.path("/products/**").uri("lb://INVENTORY-SERVICE"))
                .route(r -> r.path("/bills/**").uri("lb://BILLING-SERVICE"))
                .build();

    }

    @Bean
    public DiscoveryClientRouteDefinitionLocator dynamicRoutes(ReactiveDiscoveryClient rdc , DiscoveryLocatorProperties dlp) {
        return new DiscoveryClientRouteDefinitionLocator(rdc , dlp);
    }
}
