package kr.com.duri.groomer.domain.Enum;

import java.util.HashMap;
import java.util.Map;

public enum Matter {
    STRESS("스트레스가 있어요"),
    DISEASE("질환이 있는 친구에요");

    private final String description;
    private static final Map<String, Matter> DESCRIPTION_MAP = new HashMap<>();

    static {
        for (Matter value : Matter.values()) {
            DESCRIPTION_MAP.put(value.getDescription(), value);
        }
    }

    Matter(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Matter fromDescription(String description) {
        Matter result = DESCRIPTION_MAP.get(description);
        if (result == null) {
            throw new IllegalArgumentException("Unknown Matter description: " + description);
        }
        return result;
    }
}
