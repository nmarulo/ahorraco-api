package dev.nmarulo.ahorraco_api.commons.util;

import dev.nmarulo.ahorraco_api.commons.exception.DefaultHttpException;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.function.Supplier;

public final class UuidUtils {
    
    private UuidUtils() {
    }
    
    public static UUID toUuid(final String value, Supplier<DefaultHttpException> supplier) throws DefaultHttpException {
        if (StringUtils.isBlank(value)) {
            throw supplier.get();
        }
        
        try {
            return UUID.fromString(value.trim());
        } catch (Exception ex) {
            throw supplier.get();
        }
    }
    
}
