package net.serebryansky.common.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serebryansky.builder.model.TripBuilderResponse;
import net.serebryansky.nology.model.Trip;
import net.serebryansky.common.service.TripBuilderService;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static net.serebryansky.common.util.LoggingUtil.logOnNext;

@Slf4j
@RequiredArgsConstructor
public class TripBuilderServiceImpl implements TripBuilderService {

    private final WebClient tripBuilderClient;

    @Override
    public Mono<TripBuilderResponse> buildTrip(Trip trip) {
        // log.info("Build trip {}", trip);
        return Mono.just(trip)
                .doOnEach(logOnNext(p -> log.info("Build trip {}", p)))
                .then(Mono.deferContextual(contextView -> {
                            String requestId = contextView.get("CONTEXT_KEY");
                            String invoker = contextView.get("INVOKER");
                            return tripBuilderClient
                                    .post()
                                    .uri("build")
                                    .header("X-Request-ID", requestId)
                                    .header("X-Source", invoker)
                                    .bodyValue(trip)
                                    .retrieve()
                                    .bodyToMono(TripBuilderResponse.class);
                        }
                ));
    }
}
