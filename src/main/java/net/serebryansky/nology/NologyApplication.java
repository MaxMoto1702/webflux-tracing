package net.serebryansky.nology;

import lombok.val;
import net.serebryansky.common.filter.RequestIdFilter;
import net.serebryansky.common.service.CityService;
import net.serebryansky.common.service.PlaceService;
import net.serebryansky.common.service.RoutingService;
import net.serebryansky.common.service.TripBuilderService;
import net.serebryansky.common.service.impl.CityServiceImpl;
import net.serebryansky.common.service.impl.PlaceServiceImpl;
import net.serebryansky.common.service.impl.RoutingServiceImpl;
import net.serebryansky.common.service.impl.TripBuilderServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.function.Consumer;

@SpringBootApplication
public class NologyApplication {

    @Bean()
    public WebClient tripBuilderClient(@Value("${integrations.tripBuilderService.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean()
    public WebClient cityClient(@Value("${integrations.cityService.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean()
    public WebClient placeClient(@Value("${integrations.placeService.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean()
    public WebClient routingClient(@Value("${integrations.routingService.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public RequestIdFilter requestIdFilter() {
        return new RequestIdFilter();
    }

    @Bean
    public PlaceService placeService(WebClient placeClient) {
        return new PlaceServiceImpl(placeClient);
    }

    @Bean
    public RoutingService routingService(WebClient routingClient) {
        return new RoutingServiceImpl(routingClient);
    }

    @Bean
    public TripBuilderService tripBuilderService(WebClient tripBuilderClient) {
        return new TripBuilderServiceImpl(tripBuilderClient);
    }

    @Bean
    public CityService cityService(WebClient cityClient) {
        return new CityServiceImpl(cityClient);
    }

    public static void main(String[] args) {
        SpringApplication.run(NologyApplication.class, args);
    }

}
