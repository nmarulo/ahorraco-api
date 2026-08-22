package dev.nmarulo.ahorraco_api.commons.services;

import dev.nmarulo.ahorraco_api.app.pools.Pool;
import dev.nmarulo.ahorraco_api.app.pools.PoolRepository;
import dev.nmarulo.ahorraco_api.commons.exception.NotFoundException;
import dev.nmarulo.ahorraco_api.commons.exception.UnauthorizedException;
import dev.nmarulo.ahorraco_api.commons.util.NormalizeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessPoolService {
    
    private final PoolRepository poolRepository;
    
    @Transactional(readOnly = true)
    public Pool getByPublicId(final UUID publicId) {
        return this.poolRepository.findByPublicId(publicId)
                                  .orElseThrow(() -> new NotFoundException(
                                      "No existe ninguna porra con ese identificador."));
    }
    
    @Transactional(readOnly = true)
    public Pool findByInvitationToken(final String invitationToken) {
        final var value = StringUtils.trimToEmpty(invitationToken);
        
        return this.poolRepository.findByInvitationTokenIgnoreCase(value)
                                  .orElseThrow(() -> new NotFoundException(
                                      "Este enlace de invitación no vale o ya no existe."));
    }
    
    /**
     * Deja pasar solo al organizador de esa porra.
     */
    public void requireOrganizer(final Pool pool, final String managementCode) {
        if (isOrganizer(pool, managementCode)) {
            return;
        }
        
        throw new UnauthorizedException("El código de gestión no es el de esta porra.");
    }
    
    public boolean isOrganizer(final Pool pool, final String managementCode) {
        if (pool == null) {
            return false;
        }
        
        final var managementCodeNormalize = NormalizeUtils.trimAndUppercase(managementCode);
        
        return Objects.equals(pool.getManagementCode(), managementCodeNormalize);
    }
    
}
