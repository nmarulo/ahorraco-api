package dev.nmarulo.ahorraco_api.app.pools;

import dev.nmarulo.ahorraco_api.app.pools.dtos.CreatePoolReq;
import dev.nmarulo.ahorraco_api.app.pools.dtos.CreatePoolRes;
import dev.nmarulo.ahorraco_api.app.pools.dtos.FindInvitationPoolRes;
import dev.nmarulo.ahorraco_api.app.pools.dtos.FindPublicIdPoolRes;

public final class PoolMapper {
    
    private PoolMapper() {
    }
    
    public static CreatePoolRes toCreatePoolRes(final Pool pool) {
        final var publicId = pool.getPublicId()
                                 .toString();
        
        return new CreatePoolRes(publicId, pool.getManagementCode(), pool.getInvitationToken());
    }
    
    public static FindPublicIdPoolRes toGetPoolRes(final Pool pool, final long joinedCount, final boolean organizer) {
        final var response = new FindPublicIdPoolRes();
        
        response.setPublicId(pool.getPublicId()
                                 .toString());
        response.setName(pool.getName());
        response.setMonthlyFee(pool.getMonthlyFee());
        response.setNumParticipants(pool.getNumParticipants());
        response.setStartDate(pool.getStartDate());
        response.setPaymentDueDay(pool.getPaymentDueDay());
        response.setNotes(pool.getNotes());
        response.setJoinedCount(joinedCount);
        
        if (organizer) {
            response.setManagementCode(pool.getManagementCode());
            response.setInvitationToken(pool.getInvitationToken());
        }
        
        return response;
    }
    
    public static FindInvitationPoolRes toGetPoolInvitationRes(final Pool pool, final long joinedCount) {
        final var response = new FindInvitationPoolRes();
        
        response.setPublicId(pool.getPublicId()
                                 .toString());
        response.setName(pool.getName());
        response.setMonthlyFee(pool.getMonthlyFee());
        response.setNumParticipants(pool.getNumParticipants());
        response.setStartDate(pool.getStartDate());
        response.setPaymentDueDay(pool.getPaymentDueDay());
        response.setJoinedCount(joinedCount);
        
        return response;
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
