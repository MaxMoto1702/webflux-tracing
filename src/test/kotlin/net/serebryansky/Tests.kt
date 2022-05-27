package net.serebryansky

import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.slf4j.MDCContext
import mu.KotlinLogging
import net.serebryansky.common.util.logger
import org.junit.jupiter.api.Test
import org.slf4j.MDC
import reactor.core.publisher.Mono
import reactor.util.context.Context

class Tests {

    private val logger = KotlinLogging.logger {}

    @Test
    fun test() = runBlocking<Unit> {
//        logger.trace { "This is trace log" }
//        logger.debug { "This is debug log" }
//        logger.info { "This is info log" }
//        logger.warn { "This is warn log" }
//        logger.error { "This is error log" }
//
//        MDC.put("MDC_KEY", "rocks") // Put a value into the MDC context

//        Mono
//            .deferContextual {
//                val contextKey = it.get<String>("CONTEXT_KEY")
//                MDC.put("MDC_KEY", contextKey) // Put a value into the MDC context
//                mono(Unconfined) {
//                    launch(MDCContext()) {
//                        suspendFunction()
//                    }
//                }
//            }
//            .contextWrite(Context.of("CONTEXT_KEY", "context key"))
//            .awaitSingle()

        mono(Unconfined) {
            aopCode {
                suspendFunction()
            }
        }
            .contextWrite(Context.of("CONTEXT_KEY", "context key"))
            .awaitSingle()

//        Mono.empty<Void>()
//            .then()
//
//        mono {
//            logger.info("in mono")   // The MDC context contains the mapping here
//
//            MDC.put("MDC_KEY", "rocks") // Put a value into the MDC context
//            launch(MDCContext()) {
//                logger.info("in launch in mono")   // The MDC context contains the mapping here
//                launch(MDCContext()) {
//                    logger.info("in launch in launch in mono")   // The MDC context contains the mapping here
//                }
//            }
//
//            1
//        }
//            .doOnEach { signal: Signal<*> ->
//                val contextValue = signal.contextView.get<String>("CONTEXT_KEY")
//                MDC.putCloseable("MDC_KEY", contextValue)
//                    .use { logger.info("log info on each") }
//            }
//            .contextWrite(Context.of("CONTEXT_KEY", "context key"))
//            .awaitSingle()

//        Mono.just("123")
//            .awaitSingle()
//        launch(MDCContext()) {
//            logger().info("...")   // The MDC context contains the mapping here
//            simpleMethod()
//            flow {
//                for (i in 1..4) {
//                    emit(i)
//                    delay(1000)
//                }
//            }.collect {
//                simpleMethod(it)
//            }
//        }
    }

    private suspend fun aopCode(statement: suspend () -> Unit) {
        Mono
            .deferContextual {
                val contextKey = it.get<String>("CONTEXT_KEY")
                MDC.put("MDC_KEY", contextKey) // Put a value into the MDC context
                mono(MDCContext()) {
                    statement()
                }
            }
            .awaitSingle()
    }

    private suspend fun suspendFunction(number: Int = 0) {
        logger().info("invoke simple method : $number")   // The MDC context contains the mapping here
        delay(1000)
        logger().info("finish invoke simple method : $number")   // The MDC context contains the mapping here
    }

}
