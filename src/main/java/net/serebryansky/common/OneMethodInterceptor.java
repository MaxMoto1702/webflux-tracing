package net.serebryansky.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClassifier;
import kotlin.reflect.KFunction;
import kotlin.reflect.full.KCallables;
import kotlin.reflect.jvm.KCallablesJvm;
import kotlin.reflect.jvm.ReflectJvmMapping;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.reactive.AwaitKt;
import kotlinx.coroutines.reactive.ReactiveFlowKt;
import kotlinx.coroutines.reactor.MonoKt;
import kotlinx.coroutines.reactor.ReactorFlowKt;
import kotlinx.coroutines.slf4j.MDCContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.reactivestreams.Publisher;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.KotlinDetector;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.convert.converter.Converter;

public class OneMethodInterceptor implements MethodInterceptor {
    private static final String COROUTINES_FLOW_CLASS_NAME = "kotlinx.coroutines.flow.Flow";

    private static final int RETURN_TYPE_METHOD_PARAMETER_INDEX = -1;

    private final Converter<Object, Mono<Object>> advice = Mono::just;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Class<?> returnType = method.getReturnType();
        boolean isSuspendingFunction = KotlinDetector.isSuspendingFunction(method);
        boolean hasFlowReturnType = COROUTINES_FLOW_CLASS_NAME
                .equals(new MethodParameter(method, RETURN_TYPE_METHOD_PARAMETER_INDEX).getParameterType().getName());
        if (Mono.class.isAssignableFrom(returnType)) {
            Object result = invocation.proceed();
            return ((Mono<?>) result).flatMap(this.advice::convert).then((Mono<?>) result);
        }
        if (Flux.class.isAssignableFrom(returnType)) {
            Object result = invocation.proceed();
            return ((Flux<?>) result).flatMap(this.advice::convert).thenMany((Flux<?>) result);
        }
        if (!isSuspendingFunction && hasFlowReturnType) {
            ReactiveAdapter adapter = ReactiveAdapterRegistry.getSharedInstance().getAdapter(returnType);
            Flux<?> flux = Flux.from(adapter.toPublisher(invocation.proceed()));
            return ReactiveFlowKt.asFlow(flux.flatMap(this.advice::convert).thenMany(flux));
        }
        if (isSuspendingFunction) {
            Publisher<?> source = invokeSuspendingFunction(invocation.getMethod(), invocation.getThis(),
                    invocation.getArguments());
            Mono<?> response = Mono.from(source)
                    .flatMap((r) -> this.advice.convert(r).then(Mono.just(r)));
            return AwaitKt.awaitFirstOrNull(
                    response,
                    (Continuation<Object>) invocation.getArguments()[invocation.getArguments().length - 1]
            );
        }
        return invocation.proceed();
    }

    public static Publisher<?> invokeSuspendingFunction(Method method, Object target, Object... args) {
        final KFunction<?> function = Objects.requireNonNull(ReflectJvmMapping.getKotlinFunction(method));
        if (method.isAccessible() && !KCallablesJvm.isAccessible(function)) {
            KCallablesJvm.setAccessible(function, true);
        }
        KClassifier classifier = function.getReturnType().getClassifier();
        Mono<Object> mono = Mono.deferContextual(contextView -> {
                    String contextKey = contextView.get("CONTEXT_KEY");
                    MDC.put("MDC_KEY", contextKey); // Put a value into the MDC context
                    return MonoKt.<Object>mono(
                            ((CoroutineContext) new MDCContext()),
                            (scope, continuation) -> KCallables.<Object>callSuspend(function, getSuspendedFunctionArgs(target, args), continuation)
                    );
                })
                .filter(result -> !Objects.equals(result, Unit.INSTANCE))
                .onErrorMap(InvocationTargetException.class, InvocationTargetException::getTargetException);
        if (classifier != null && classifier.equals(JvmClassMappingKt.getKotlinClass(Flow.class))) {
            return mono.flatMapMany(OneMethodInterceptor::asFlux);
        }
        return mono;
    }

    private static Object[] getSuspendedFunctionArgs(Object target, Object... args) {
        Object[] functionArgs = new Object[args.length];
        functionArgs[0] = target;
        System.arraycopy(args, 0, functionArgs, 1, args.length - 1);
        return functionArgs;
    }

    private static Flux<?> asFlux(Object flow) {
        return ReactorFlowKt.asFlux(((Flow<?>) flow));
    }
}
