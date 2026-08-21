package dev.nmarulo.ahorraco_api.commons.util;

public final class IntegerUtils {
    
    private IntegerUtils() {
    }
    
    public static boolean isInRange(Integer value, int start, int end) {
        return value != null && value >= start && value <= end;
    }
    
}
