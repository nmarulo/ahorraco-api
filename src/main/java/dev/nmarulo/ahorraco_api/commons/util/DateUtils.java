package dev.nmarulo.ahorraco_api.commons.util;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public final class DateUtils {
    
    private DateUtils() {
    }
    
    public static boolean isDateEqualOrAfterCurrentMonth(final LocalDate date) {
        if (date == null) {
            return false;
        }
        
        final var now = nowWithFirstDayMonth();
        final var currentDate = withFirstDayMonth(date);
        
        return currentDate.isEqual(now) || currentDate.isAfter(now);
    }
    
    public static boolean isDateEqualOrBeforeCurrentMonth(final LocalDate date) {
        if (date == null) {
            return false;
        }
        
        final var now = nowWithFirstDayMonth();
        final var currentDate = withFirstDayMonth(date);
        
        return currentDate.isEqual(now) || currentDate.isBefore(now);
    }
    
    public static LocalDate nowWithFirstDayMonth() {
        return withFirstDayMonth(LocalDate.now());
    }
    
    public static LocalDate withFirstDayMonth(LocalDate localDate) {
        return localDate.withDayOfMonth(1);
    }
    
    public static String getFullDisplayNameMonth(LocalDate localDate, Locale locale) {
        return localDate.getMonth()
                        .getDisplayName(TextStyle.FULL, locale);
    }
    
}
