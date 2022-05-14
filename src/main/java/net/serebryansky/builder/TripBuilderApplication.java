package net.serebryansky.builder;

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

    @Bean()
    public WebClient routingClient(@Value("${integrations.routingService.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(TripBuilderApplication.class, args);
    }

}
