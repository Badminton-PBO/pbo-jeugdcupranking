package be.pbo.jeugdcup.ranking.domain;

public enum AgeCategory {

    MINIBAD, U11, U13, U15, U17, UNKNOWN;

    public static final AgeCategory DEFAULT_AGE_CATEGORY = AgeCategory.UNKNOWN;
}
