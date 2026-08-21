package dev.nmarulo.ahorraco_api.commons.component;

import org.springframework.boot.jackson.JacksonComponent;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@JacksonComponent
public final class LocalDateJacksonComponent {
    
    public static class LocalDateValueDeserializer extends ValueDeserializer<LocalDate> {
        
        /**
         * A partir de aquí el número solo tiene sentido leído como milisegundos.
         */
        private static final long MILLISECONDS_THRESHOLD = 100_000_000_000L;
        
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
            if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                final var instant = getInstant(p.getLongValue());
                
                return LocalDate.ofInstant(instant, ZoneId.systemDefault());
            }
            
            return LocalDateDeserializer.INSTANCE.deserialize(p, ctxt);
        }
        
        private Instant getInstant(long longValue) {
            if (Math.abs(longValue) < MILLISECONDS_THRESHOLD) {
                return Instant.ofEpochSecond(longValue);
            }
            
            return Instant.ofEpochMilli(longValue);
        }
        
    }
    
}
