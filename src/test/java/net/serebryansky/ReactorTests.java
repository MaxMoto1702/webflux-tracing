package net.serebryansky;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

import static net.serebryansky.nology.LoggingUtil.logOnEach;

public class ReactorTests {

    private static final Logger log = LoggerFactory.getLogger(ReactorTests.class);

    @Test
    public void test() {
        Flux<String> ids = ifhrIds();

        Flux<String> combinations =
                ids.flatMap(id -> {
                    Mono<String> nameTask = ifhrName(id);
                    Mono<Integer> statTask = ifhrStat(id);

                    return nameTask.zipWith(statTask,
                            (name, stat) -> "Name " + name + " has stats " + stat);
                });

        Mono<List<String>> result = combinations.collectList();

        List<String> results = result.block();
        System.out.println(results);
    }

    private Mono<Integer> ifhrStat(String id) {
        return Mono
                .just(id)
                .map(Integer::parseInt);
    }

    private Mono<String> ifhrName(String id) {
        return Mono.just("#" + id);
    }

    private Flux<String> ifhrIds() {
        return Flux
                .range(1, 10)
                .map(String::valueOf);
    }

    @Test
    public void test2() {
        String key = "message";
        Flux<String> r = Flux
                .range(1, 10)
//                .parallel(3)
                .flatMap(this::method1)
                .flatMap(this::method2)
//                .flatMap(this::method1)
                .flatMap(s -> Mono.deferContextual(ctx -> method3(s, ctx.get(key))))
                .flatMap(this::method4)
                .doOnEach(logOnEach(s -> log.info("Message: {}", s)))
//                .sequential()
                .contextWrite(ctx -> ctx.put(key, "World"));

        System.out.println(r.blockLast());
    }

    private Mono<String> method4(Integer integer) {
        return Mono.just(integer)
                .flatMap(i1 -> Mono.deferContextual(contextView -> Mono.just("Response from another system by " + i1 + " and request id " + contextView.get("message"))));
    }

    public Mono<String> method1(Integer i) {
        long round = Math.round(Math.random());
        return Mono.just(i)
                .delayElement(Duration.ofSeconds(round))
                .doOnEach(logOnEach(s -> log.info("Method 1: {}", s)))
                .map(String::valueOf);
    }

    public Mono<Integer> method2(String s) {
        return Mono.just(s)
                .doOnEach(logOnEach(i -> log.info("Method 2: {}", i)))
                .map(Integer::parseInt);
    }

    public Mono<Integer> method3(Integer i, String s) {
        return Mono.just(i)
                .doOnEach(logOnEach(i1 -> log.info("Method 3: {} and use request id {}", i1, s)));
    }

}
