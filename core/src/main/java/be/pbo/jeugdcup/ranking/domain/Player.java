package be.pbo.jeugdcup.ranking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Player {
    private Integer id;
    private String firstName;
    private String lastName;
    private String memberId;
    private Gender gender; // 1=M, 2=V
    private String clubName;
    private AgeCategory ageCategory = AgeCategory.DEFAULT_AGE_CATEGORY;
    private List<String> eventName = new ArrayList<>();
}
