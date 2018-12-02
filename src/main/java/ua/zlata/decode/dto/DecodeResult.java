package ua.zlata.decode.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DecodeResult {

    private List<String> examples = new ArrayList<>();

    private List<Hint> hints = new ArrayList<>();

    public void addExample(String example) {
        examples.add(example);
    }

    public void addHint(Hint hint) {
        hints.add(hint);
    }
}
