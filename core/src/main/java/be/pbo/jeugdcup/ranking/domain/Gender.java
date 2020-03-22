package be.pbo.jeugdcup.ranking.domain;

public enum Gender {
    MALE("M"), FEMALE("F"), UNKNOWN("X");

    final String genderShort;

    Gender(final String genderShort) {
        this.genderShort = genderShort;
    }

    public String getGenderShort() {
        return this.genderShort;
    }
}
