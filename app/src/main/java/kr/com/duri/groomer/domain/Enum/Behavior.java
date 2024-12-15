package kr.com.duri.groomer.domain.Enum;

import java.util.HashMap;
import java.util.Map;

public enum Behavior {
    AVOID("피하려는 행동이 있어요"),
    BRAVEST("왕왕!내가 제일 용맹하개"),
    NO_REACTION("별다른 반응이 없어요");

    private final String description;
    private static final Map<String, Behavior> DESCRIPTION_MAP = new HashMap<>();

    static {
        for (Behavior value : Behavior.values()) {
            DESCRIPTION_MAP.put(value.getDescription(), value);
        }
    }

    Behavior(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Behavior fromDescription(String description) {
        Behavior result = DESCRIPTION_MAP.get(description);
        if (result == null) {
            throw new IllegalArgumentException("Unknown Behavior description: " + description);
        }
        return result;
    }
}
