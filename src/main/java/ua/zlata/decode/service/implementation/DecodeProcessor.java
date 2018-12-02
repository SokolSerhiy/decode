package ua.zlata.decode.service.implementation;

import reactor.core.publisher.Mono;
import ua.zlata.decode.dto.DecodeResult;
import ua.zlata.decode.dto.Hint;
import ua.zlata.decode.dto.Operation;
import ua.zlata.decode.service.Processor;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DecodeProcessor implements Processor<DecodeResult> {

    private final static int ONE = 1;

    private final static int ZERO = 0;

    private final String word;

    private final List<Operation> operations;

    private final int maxRes;

    private final Set<Integer> usedHints = new HashSet<>();

    private final Set<String> usedExamples = new HashSet<>();

    private final DecodeResult result = new DecodeResult();

    public DecodeProcessor(String word, List<Operation> operations, int maxRes) {
        this.word = word;
        this.operations = operations;
        this.maxRes = maxRes;
    }

    @Override
    public Mono<DecodeResult> process() {
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (word.indexOf(ch) == i) {
                generateExample(ch);
            } else {
                generateNewExample(ch);
            }
        }
        Collections.shuffle(result.getHints());
        return Mono.just(result);
    }

    private void generateNewExample(char ch) {
        Operation operation = chooseOperation();
        int hintNumber = peekHintNumber(ch);
        String example = generateExample(operation, hintNumber);
        if (usedExamples.add(example)) {
            result.addExample(example);
        } else {
            generateNewExample(ch);
        }
    }

    private String generateExample(Operation operation, int hintNumber) {
        int firstOperand = getFirstOperand(operation, hintNumber);
        int secondOperand = getSecondOperand(operation, hintNumber, firstOperand);
        return buildExample(firstOperand, operation, secondOperand);
    }

    private int peekHintNumber(char ch) {
        return result.getHints().stream().filter(h -> h.getLetter() == ch).findFirst().get().getNumber();
    }

    private void generateExample(char ch) {
        Operation operation = chooseOperation();
        int hintNumber = generateHintNumber(operation);
        String example = generateExample(operation, hintNumber);
        if (usedExamples.add(example)) {
            result.addExample(example);
            result.addHint(new Hint(ch, hintNumber));
        } else {
            generateExample(ch);
        }
    }


    private String buildExample(int firstOperand, Operation operation, int secondOperand) {
        return firstOperand + " " + operation.getSymbol() + " " + secondOperand + " =";
    }

    private int getSecondOperand(Operation operation, int hintNumber, int firstOperand) {
        switch (operation) {
            case ADD: return hintNumber - firstOperand;
            case SUB: return firstOperand - hintNumber;
            default: throw new IllegalArgumentException("Others methods not supported");
        }
    }

    private int getFirstOperand(Operation operation, int hintNumber) {
        switch (operation) {
            case ADD: return random(ONE, hintNumber - ONE);
            case SUB: return random(hintNumber + ONE, maxRes);
            default: throw new IllegalArgumentException("Others methods not supported");
        }
    }

    private int generateHintNumber(Operation operation) {
        return uniqueHintRandom(ONE + ONE, maxRes - ONE);
    }

    private Operation chooseOperation() {
        return operations.get(random(ZERO, operations.size() - ONE));
    }

    private int random(int min, int max) {
        return (int)Math.round(Math.random()*(max - min) + min);
    }

    private int uniqueHintRandom(int min, int max) {
        int random = random(min, max);
        if (usedHints.add(random)) {
            return random;
        } else {
            return uniqueHintRandom(min, max);
        }
    }
}
