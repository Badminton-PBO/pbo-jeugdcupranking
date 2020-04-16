package be.pbo.jeugdcup.ranking.domain;

import lombok.extern.slf4j.Slf4j;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AgeCategoryDetector {

    private static final Pattern AGE_CATEGORY_PATTERN = Pattern.compile("(U\\d\\d)");

    public AgeCategory resolveFromEventName(final String eventName) {

        if (eventName != null) {
            String normalizedEventName = eventName.toUpperCase().trim();
            normalizedEventName = normalizedEventName.replaceAll("[-_\\s]", "");

            final Set<AgeCategory> foundAgeCategories = new HashSet<>();
            if (normalizedEventName.contains("MINI")) {
                foundAgeCategories.add(AgeCategory.MINIBAD);
            }

            final Matcher matcher = AGE_CATEGORY_PATTERN.matcher(normalizedEventName);
            while (matcher.find()) {
                String mgroup = "";
                try {
                    mgroup = matcher.group();
                    if ("U17".equals(mgroup) || "U19".equals(mgroup)) {
                        foundAgeCategories.add(AgeCategory.U17U19);
                    } else {
                        foundAgeCategories.add(AgeCategory.valueOf(mgroup));
                    }
                } catch (final IllegalArgumentException e) {
                    log.warn("Unable to convert '" + mgroup + "' into a known AgeCategory");
                    foundAgeCategories.add(AgeCategory.UNKNOWN);
                }
            }

            if (foundAgeCategories.size() > 1) {
                log.warn("Unable to convert Event name '" + eventName + "' into a single AgeCategory. Multiple AgeCategories found " + foundAgeCategories);
                return AgeCategory.UNKNOWN;
            } else if (foundAgeCategories.size() == 1) {
                return foundAgeCategories.iterator().next();
            } else {
                log.warn("Unable to convert Event name '" + eventName + "' into a known AgeCategory");
                return AgeCategory.UNKNOWN;
            }
        }
        return AgeCategory.DEFAULT_AGE_CATEGORY;
    }
}
