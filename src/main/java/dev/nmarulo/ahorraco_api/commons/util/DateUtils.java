package dev.nmarulo.ahorraco_api.commons.util;

import java.time.LocalDate;

public final class DateUtils {
    
    private DateUtils() {
    }
    
    public static boolean isDateNotBeforeCurrentMonth(final LocalDate date) {
        if (date == null) {
            return false;
        }
        
        final var now = nowWithFirstDayMonth();
        
        return date.isEqual(now) || date.isAfter(now);
    }
    
    public static LocalDate nowWithFirstDayMonth() {
        return LocalDate.now()
                        .withDayOfMonth(1);
    }
    
}
