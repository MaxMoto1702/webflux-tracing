package net.serebryansky.common.util

import org.slf4j.MDC
import reactor.core.publisher.Signal
import java.util.function.Consumer

fun logOnEach(logStatement: Runnable): Consumer<Signal<*>> {
    return Consumer { signal: Signal<*> ->
        val contextValue = signal.contextView.get<String>("CONTEXT_KEY")
        val invoker = signal.contextView.get<String>("INVOKER")
        MDC.putCloseable("INVOKER_KEY", invoker)
                .use {
                    MDC.putCloseable("MDC_KEY", contextValue)
                            .use { logStatement.run() }
                }
    }
}

fun <T> logOnEach(logStatement: Consumer<T?>): Consumer<Signal<T?>> {
    return Consumer { signal: Signal<T?> ->
        val contextValue = signal.contextView.get<String>("CONTEXT_KEY")
        val invoker = signal.contextView.get<String>("INVOKER")
        MDC.putCloseable("INVOKER_KEY", invoker).use { MDC.putCloseable("MDC_KEY", contextValue).use { logStatement.accept(signal.get()) } }
    }
}

fun <T> logOnNext(logStatement: Consumer<T>): Consumer<Signal<T>> {
    return Consumer { signal: Signal<T> ->
        if (signal.isOnNext) {
            val contextValue = signal.contextView.get<String>("CONTEXT_KEY")
            val invoker = signal.contextView.get<String>("INVOKER")
            MDC.putCloseable("INVOKER_KEY", invoker).use { MDC.putCloseable("MDC_KEY", contextValue).use { logStatement.accept(signal.get()!!) } }
        }
    }
}
