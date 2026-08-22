package dev.nmarulo.ahorraco_api.commons.util;

import org.apache.commons.lang3.StringUtils;

public final class NormalizeUtils {
    
    private NormalizeUtils() {
    }
    
    public static String trimAndUppercase(final String value) {
        return StringUtils.trimToEmpty(value)
                          .toUpperCase();
    }
    
}
