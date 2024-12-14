package kr.com.duri.groomer.domain.Enum;

import java.util.HashMap;
import java.util.Map;

public enum Reaction {
    AGGRESSIVE("다소 공격적이에요"),
    AVOID("미용도구를 피해요"),
    NO_REACTION("별다른 반응이 없어요");

    private final String description;
    private static final Map<String, Reaction> DESCRIPTION_MAP = new HashMap<>();

    static {
        for (Reaction value : Reaction.values()) {
            DESCRIPTION_MAP.put(value.getDescription(), value);
        }
    }

    Reaction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Reaction fromDescription(String description) {
        Reaction result = DESCRIPTION_MAP.get(description);
        if (result == null) {
            throw new IllegalArgumentException("Unknown Reaction description: " + description);
        }
        return result;
    }
}
