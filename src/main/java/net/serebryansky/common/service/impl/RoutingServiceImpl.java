package net.serebryansky.common.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serebryansky.common.service.RoutingService;
import net.serebryansky.place.model.Place;
import net.serebryansky.routing.model.DurationMatrixResponse;
import net.serebryansky.routing.model.Route;
import net.serebryansky.routing.model.RouteSearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static net.serebryansky.common.util.LoggingUtil.logOnNext;

@Slf4j
@RequiredArgsConstructor
public class RoutingServiceImpl implements RoutingService {
    private final WebClient routingClient;

    @Override
    public Mono<DurationMatrixResponse> getDurationMatrix(List<Place> p) {
        return Mono.just(p)
                .doOnEach(logOnNext(p1 -> log.info("Get duration matrix for {}", p1)))
                .then(Mono.deferContextual(contextView -> {
                    String requestId = contextView.get("CONTEXT_KEY");
                    String invoker = contextView.get("INVOKER");
                    return routingClient.post()
                            .uri("/durations/matrix")
                            .header("X-Request-ID", requestId)
                            .header("X-Source", invoker)
                            .bodyValue(p)
                            .retrieve()
                            .bodyToMono(DurationMatrixResponse.class);
                }));
    }

    @Override
    public Mono<Route> getRoute(List<Place> placePair) {
        // log.info("Get route between {} and {}", placePair.get(0), placePair.get(1));
        return Mono.just(placePair)
                .doOnEach(logOnNext(p -> log.info("Get route between {} and {}", p.get(0), p.get(1))))
                .then(Mono.deferContextual(contextView -> {
                    String requestId = contextView.get("CONTEXT_KEY");
                    String invoker = contextView.get("INVOKER");
                    return routingClient
                            .post()
                            .uri("/routes/search")
                            .header("X-Request-ID", requestId)
                            .header("X-Source", invoker)
                            .bodyValue(new RouteSearchRequest(placePair.get(0), placePair.get(1)))
                            .retrieve()
                            .bodyToMono(Route.class);
                }));
    }
}
