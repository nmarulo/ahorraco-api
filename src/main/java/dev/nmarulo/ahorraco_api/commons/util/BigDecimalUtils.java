package dev.nmarulo.ahorraco_api.commons.util;

import java.math.BigDecimal;

public final class BigDecimalUtils {
    
    private BigDecimalUtils() {
    }
    
    public static boolean isGreaterThanZero(final BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
    
}
