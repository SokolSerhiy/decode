package ua.zlata.decode.service;

import reactor.core.publisher.Mono;
import ua.zlata.decode.dto.DecodeResult;
import ua.zlata.decode.dto.DecodeWord;
import ua.zlata.decode.dto.Operation;

import java.util.List;

public interface DecodeService {

    Mono<DecodeResult> decode(DecodeWord word, List<Operation> operations, int maxRes);
}
