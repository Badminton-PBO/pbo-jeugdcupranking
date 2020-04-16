package be.pbo.jeugdcup.ranking.domain;

import lombok.Getter;

@Getter
public enum AgeCategory {

    MINIBAD("Minibad", 0), U11("U11", 1), U13("U13", 2), U15("U15", 3), U17U19("U17-U19", 4), UNKNOWN("Unknown", 5);

    public static final AgeCategory DEFAULT_AGE_CATEGORY = AgeCategory.UNKNOWN;

    private final String key;
    private final int index; // When player plays in events with different AgeCategories, the AgeCategory with the lowest index should remain

    AgeCategory(final String key, final int index) {
        this.key = key;
        this.index = index;
    }

}
