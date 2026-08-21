package dev.nmarulo.ahorraco_api.commons.util;

import java.security.SecureRandom;

public final class CodeGenerator {
    
    /**
     * Caracteres legibles: sin `I`, `L`, `O`, `0` ni `1`.
     */
    private static final String ALPHABET = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    
    private static final int SUFFIX_MANAGEMENT_CODE_LENGTH = 5;
    
    private static final int INVITATION_TOKEN_LENGTH = 10;
    
    private static final String DEFAULT_INITIALS_MANAGEMENT_CODE = "HRR";
    
    private static final SecureRandom RANDOM = new SecureRandom();
    
    private CodeGenerator() {
    }
    
    /**
     * Compone el código de gestión del organizador: las iniciales fijas y un sufijo aleatorio, por ejemplo `HRR-K7X2`.
     */
    public static String managementCode() {
        final var randomString = randomString(SUFFIX_MANAGEMENT_CODE_LENGTH).toUpperCase();
        
        return "%s-%s".formatted(DEFAULT_INITIALS_MANAGEMENT_CODE, randomString);
    }
    
    public static String invitationToken() {
        return randomString(INVITATION_TOKEN_LENGTH).toUpperCase();
    }
    
    /**
     * Cadena aleatoria del alfabeto legible, de la longitud pedida.
     */
    private static String randomString(final int length) {
        final var result = new StringBuilder(length);
        
        for (var index = 0; index < length; index++) {
            result.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        
        return result.toString();
    }
    
}
