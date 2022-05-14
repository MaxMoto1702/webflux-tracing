package net.serebryansky.common.util;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;

import java.util.function.Consumer;

public class LoggingUtil {
    private LoggingUtil() {
        throw new RuntimeException();
    }

    public static Consumer<Signal<?>> logOnEach(Runnable logStatement) {
        return signal -> {
            String contextValue = signal.getContextView().get("CONTEXT_KEY");
            String invoker = signal.getContextView().get("INVOKER");
            try (MDC.MDCCloseable cMdc = MDC.putCloseable("MDC_KEY", contextValue); MDC.MDCCloseable cMdc2 = MDC.putCloseable("INVOKER_KEY", contextValue)) {
                logStatement.run();
            }
        };
    }

    public static <T> Consumer<Signal<T>> logOnEach(Consumer<T> logStatement) {
        return signal -> {
            String contextValue = signal.getContextView().get("CONTEXT_KEY");
            try (MDC.MDCCloseable cMdc = MDC.putCloseable("MDC_KEY", contextValue)) {
                logStatement.accept(signal.get());
            }
        };
    }

    public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (signal.isOnNext()) {
                String contextValue = signal.getContextView().get("CONTEXT_KEY");
                try (MDC.MDCCloseable cMdc = MDC.putCloseable("MDC_KEY", contextValue)) {
                    logStatement.accept(signal.get());
                }
            }
        };
    }
}
