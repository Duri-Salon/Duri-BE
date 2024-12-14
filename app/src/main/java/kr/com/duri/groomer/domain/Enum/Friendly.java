package kr.com.duri.groomer.domain.Enum;

import java.util.HashMap;
import java.util.Map;

public enum Friendly {
    HIGH("베스트 프렌드"),
    MEDIUM("라뽀가 많이 형성됐어요"),
    LOW("우리 조금 어색해요");

    private final String description;
    private static final Map<String, Friendly> DESCRIPTION_MAP = new HashMap<>();

    static {
        for (Friendly value : Friendly.values()) {
            DESCRIPTION_MAP.put(value.getDescription(), value);
        }
    }

    Friendly(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Friendly fromDescription(String description) {
        Friendly result = DESCRIPTION_MAP.get(description);
        if (result == null) {
            throw new IllegalArgumentException("Unknown Friendly description: " + description);
        }
        return result;
    }
}
