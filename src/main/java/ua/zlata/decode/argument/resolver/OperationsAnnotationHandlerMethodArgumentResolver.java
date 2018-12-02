package ua.zlata.decode.argument.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ua.zlata.decode.argument.annotation.Operations;
import ua.zlata.decode.dto.Operation;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperationsAnnotationHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Optional<Operations> optional = getAnnotation(methodParameter);
        return optional.isPresent();
    }

    private Optional<Operations> getAnnotation(MethodParameter methodParameter) {
        return Stream.of(methodParameter.getParameterAnnotations()).filter(this::isEqual).map(Operations.class::cast).findFirst();
    }

    private boolean isEqual(Annotation a) {
        return a instanceof Operations;
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter methodParameter, BindingContext bindingContext, ServerWebExchange serverWebExchange) {
        MultiValueMap<String, String> params = serverWebExchange.getRequest().getQueryParams();
        Optional<Operations> optional = getAnnotation(methodParameter);
        if (optional.isPresent() && params.get(optional.get().name()) == null) {
            return Mono.just(Arrays.asList(Operation.ADD, Operation.SUB));
        }
        List<Operation> res = Stream.of(optional.get()).map(Operations::name).map(params::get).flatMap(List::stream).map(String::toUpperCase).map(Operation::valueOf).collect(Collectors.toList());
        return Mono.just(res);
    }
}
