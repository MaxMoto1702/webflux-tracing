package net.serebryansky.builder;

import net.serebryansky.common.filter.RequestIdFilter;
import net.serebryansky.common.service.PlaceService;
import net.serebryansky.common.service.RoutingService;
import net.serebryansky.common.service.impl.PlaceServiceImpl;
import net.serebryansky.common.service.impl.RoutingServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class TripBuilderApplication {

    @Bean()
    public WebClient placeClient(@Value("${integrations.placeService.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public RequestIdFilter requestIdFilter() {
        return new RequestIdFilter();
    }

    @Bean()
    public WebClient routingClient(@Value("${integrations.routingService.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public PlaceService placeService(WebClient placeClient, String applicationName) {
        return new PlaceServiceImpl(placeClient, applicationName);
    }

    @Bean
    public String applicationName() {
        return "trip-builder";
    }

    @Bean
    public RoutingService routingService(WebClient routingClient, String applicationName) {
        return new RoutingServiceImpl(routingClient, applicationName);
    }

    public static void main(String[] args) {
        SpringApplication.run(TripBuilderApplication.class, args);
    }

}
