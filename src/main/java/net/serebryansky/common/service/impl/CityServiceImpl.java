package net.serebryansky.common.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serebryansky.city.model.City;
import net.serebryansky.common.service.CityService;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static net.serebryansky.common.util.LoggingUtil.logOnNext;

@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final WebClient cityClient;

    @Override
    public Mono<City> getCity(String cityId) {
        // log.info("Get city {}", cityId);
        return Mono.just(cityId)
                .doOnEach(logOnNext(p -> log.info("Get city {}", p)))
                .then(Mono.deferContextual(contextView -> {
                            String requestId = contextView.get("CONTEXT_KEY");
                            String invoker = contextView.get("INVOKER");
                            return cityClient.get()
                                    .uri("/cities/{id}", cityId)
                                    .header("X-Request-ID", requestId)
                                    .header("X-Source", invoker)
                                    .retrieve()
                                    .bodyToMono(City.class);
                        }
                ));
    }
}
