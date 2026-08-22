package dev.nmarulo.ahorraco_api.commons.services;

import dev.nmarulo.ahorraco_api.app.pools.Pool;
import dev.nmarulo.ahorraco_api.app.pools.PoolRepository;
import dev.nmarulo.ahorraco_api.commons.exception.NotFoundException;
import dev.nmarulo.ahorraco_api.commons.util.NormalizeUtils;
import lombok.RequiredArgsConstructor;
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
    
    public boolean isOrganizer(final Pool pool, final String managementCode) {
        if (pool == null) {
            return false;
        }
        
        final var managementCodeNormalize = NormalizeUtils.trimAndUppercase(managementCode);
        
        return Objects.equals(pool.getManagementCode(), managementCodeNormalize);
    }
    
}
