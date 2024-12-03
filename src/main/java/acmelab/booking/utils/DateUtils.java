package acmelab.booking.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static boolean isAfterStartOfThisWeek(Instant date) {
        Instant now = Instant.now();
        Instant startOfWeek = now.truncatedTo(ChronoUnit.DAYS)
                .minus(now.atZone(ZoneOffset.UTC).getDayOfWeek().getValue() - 1, ChronoUnit.DAYS);

        return !date.isBefore(startOfWeek);
    }

    public static boolean isOlderThanStartOfThisWeek(Instant date) {
        return !isAfterStartOfThisWeek(date);
    }
}
