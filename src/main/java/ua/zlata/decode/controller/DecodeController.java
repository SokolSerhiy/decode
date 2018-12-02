package ua.zlata.decode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.zlata.decode.argument.annotation.Operations;
import ua.zlata.decode.dto.DecodeResult;
import ua.zlata.decode.dto.DecodeWord;
import ua.zlata.decode.dto.Operation;
import ua.zlata.decode.service.DecodeService;

import java.util.List;

@Controller
@RequestMapping("/decode")
public class DecodeController {

    private final DecodeService service;

    public DecodeController(DecodeService service) {
        this.service = service;
    }

    @PostMapping
    public @ResponseBody Mono<DecodeResult> result(@RequestBody DecodeWord word,
                                     @Operations List<Operation> operations,
                                     @RequestParam(name = "max_res", defaultValue = "10", required = false) Integer maxRes) {
        if (word.getWord() == null || word.getWord().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Word cannot be empty"));
        }
        if (word.getWord().length() >= maxRes - 1) {
            return Mono.error(new IllegalArgumentException("Word must be shorter then max result - 1"));
        }
        return service.decode(word, operations, maxRes);
    }
}
