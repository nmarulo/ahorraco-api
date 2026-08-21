package dev.nmarulo.ahorraco_api.commons.util;

import java.time.LocalDate;

public final class DateUtils {
    
    private DateUtils() {
    }
    
    public static boolean isDateNotBeforeCurrentMonth(final LocalDate date) {
        if (date == null) {
            return false;
        }
        
        final var now = LocalDate.now()
                                 .withDayOfMonth(1);
        
        return date.isEqual(now) || date.isAfter(now);
    }
    
}
