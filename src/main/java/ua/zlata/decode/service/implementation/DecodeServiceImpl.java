package ua.zlata.decode.service.implementation;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.zlata.decode.dto.DecodeResult;
import ua.zlata.decode.dto.DecodeWord;
import ua.zlata.decode.dto.Operation;
import ua.zlata.decode.service.DecodeService;
import ua.zlata.decode.service.Processor;

import java.util.List;

@Service
public class DecodeServiceImpl implements DecodeService {

    @Override
    public Mono<DecodeResult> decode(DecodeWord word, List<Operation> operations, int maxRes) {
        Processor<DecodeResult> processor = new DecodeProcessor(word.getWord(), operations, maxRes);
        return Mono.defer(processor::process);
    }
}
