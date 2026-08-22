package dev.nmarulo.ahorraco_api.commons.util;

import java.time.LocalDate;

public final class DateUtils {
    
    private DateUtils() {
    }
    
    public static boolean isDateEqualOrAfterCurrentMonth(final LocalDate date) {
        if (date == null) {
            return false;
        }
        
        final var now = nowWithFirstDayMonth();
        
        return date.isEqual(now) || date.isAfter(now);
    }
    
    public static boolean isDateEqualOrBeforeCurrentMonth(final LocalDate date) {
        if (date == null) {
            return false;
        }
        
        final var now = nowWithFirstDayMonth();
        
        return date.isEqual(now) || date.isBefore(now);
    }
    
    public static LocalDate nowWithFirstDayMonth() {
        return withFirstDayMonth(LocalDate.now());
    }
    
    public static LocalDate withFirstDayMonth(LocalDate localDate) {
        return localDate.withDayOfMonth(1);
    }
    
}
