package dev.nmarulo.ahorraco_api.app.pools;

import dev.nmarulo.ahorraco_api.app.pools.dtos.CreatePoolReq;
import dev.nmarulo.ahorraco_api.app.pools.dtos.CreatePoolRes;

public final class PoolMapper {
    
    private PoolMapper() {
    }
    
    public static CreatePoolRes toCreatePoolRes(final Pool pool) {
        final var publicId = pool.getPublicId()
                                 .toString();
        
        return new CreatePoolRes(publicId, pool.getManagementCode(), pool.getInvitationToken());
    }
    
    public static Pool toPool(CreatePoolReq request) {
        final var pool = new Pool();
        
        pool.setName(request.getName());
        pool.setMonthlyFee(request.getMonthlyFee());
        pool.setNumParticipants(request.getNumParticipants());
        pool.setStartDate(request.getStartDate());
        pool.setPaymentDueDay(request.getPaymentDueDay());
        pool.setNotes(request.getNotes());
        
        return pool;
    }
    
}
