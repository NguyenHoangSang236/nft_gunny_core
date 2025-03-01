package com.nftgunny.core.config.constant;

public enum SystemConfigCriteria {
    GOLD_PER_GAME("Gold per game"),
    CHARACTER_MAX_LEVEL("Character max level"),
    MIN_WIND_SPEED("Min wind speed"),
    MAX_WIND_SPEED("Max wind speed"),
    ATTACK_WEIGHT("Attack weight"),
    DEFENSE_WEIGHT("Defense weight"),
    MAGIC_WEIGHT("Magic weight"),
    CHARACTER_DEFAULT_SPEED("Character default speed");

    private final String value;

    SystemConfigCriteria(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
