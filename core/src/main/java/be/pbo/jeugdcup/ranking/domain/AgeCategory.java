package be.pbo.jeugdcup.ranking.domain;

import lombok.Getter;

@Getter
public enum AgeCategory {

    MINIBAD("Minibad"), U11("U11"), U13("U13"), U15("U15"), U17U19("U17-U19"), UNKNOWN("Unknown");

    public static final AgeCategory DEFAULT_AGE_CATEGORY = AgeCategory.UNKNOWN;

    private final String key;
    AgeCategory(final String key) {
        this.key = key;
    }

}
