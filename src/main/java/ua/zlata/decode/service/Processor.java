package ua.zlata.decode.service;

import reactor.core.publisher.Mono;

public interface Processor<T> {

    Mono<T> process();
}
