package net.serebryansky.nology;

import lombok.val;
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

    public static void main(String[] args) {
        SpringApplication.run(NologyApplication.class, args);
    }

}
