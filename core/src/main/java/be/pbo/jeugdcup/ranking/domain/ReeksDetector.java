package be.pbo.jeugdcup.ranking.domain;

import lombok.extern.slf4j.Slf4j;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ReeksDetector {

    private static final Pattern REEKS_PATTERN = Pattern.compile("([A|B])([_\\s]REEKS)?$");

    public Reeks resolveFromEventName(final String eventName) {
        Reeks result = Reeks.NA;

        if (eventName != null) {
            String normalizedEventName = eventName.toUpperCase().trim();
            normalizedEventName = normalizedEventName.replaceAll("-", "_");

            final Matcher matcher = REEKS_PATTERN.matcher(normalizedEventName);
            if (matcher.find()) {
                try {
                    final String mgroup1 = matcher.group(1);

                    result = Reeks.valueOf(mgroup1+"_REEKS");
                } catch (final IllegalArgumentException e) {
                    log.warn("Unable to convert Event name " + eventName + " into a known Reeks");
                }
            }
        }
        return result;
    }
}
