package dev.nmarulo.ahorraco_api.commons.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class BigDecimalUtils {
    
    private BigDecimalUtils() {
    }
    
    public static boolean isGreaterThanZero(final BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public static String twoFractionDigitsNumberFormat(BigDecimal bigDecimal, Locale locale) {
        final var format = NumberFormat.getNumberInstance(locale);
        
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        
        return format.format(bigDecimal);
    }
    
}
