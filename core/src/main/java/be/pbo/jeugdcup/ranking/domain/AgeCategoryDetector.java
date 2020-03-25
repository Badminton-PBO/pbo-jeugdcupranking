package be.pbo.jeugdcup.ranking.domain;

import lombok.extern.slf4j.Slf4j;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AgeCategoryDetector {

    private static final Pattern AGE_CATEGORY_PATTERN = Pattern.compile("((U\\d\\d){1,2})");

    public AgeCategory resolveFromEventName(final String eventName) {
        AgeCategory result = AgeCategory.DEFAULT_AGE_CATEGORY;

        if (eventName != null) {
            String normalizedEventName = eventName.toUpperCase().trim();
            normalizedEventName = normalizedEventName.replaceAll("[-_\\s]", "");

            if (normalizedEventName.contains("MINI")) {
                return AgeCategory.MINIBAD;
            }
            final Matcher matcher = AGE_CATEGORY_PATTERN.matcher(normalizedEventName);
            if (matcher.find()) {
                try {
                    final String mgroup1 = matcher.group();
                    if ("U17".equals(mgroup1) || "U19".equals(mgroup1)) {
                        return AgeCategory.U17U19;
                    }
                    result = AgeCategory.valueOf(mgroup1);
                } catch (final IllegalArgumentException e) {
                    log.warn("Unable to convert Event name " + eventName + " into a known AgeCategory");
                }
            }
        }
        return result;
    }
}
