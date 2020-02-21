package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;

// In Dutch called 'Kwalificatieschema"
@Data
public class QualificationScheme extends Draw {

    @Override
    public Boolean isValid() {
        return null;
    }
}
