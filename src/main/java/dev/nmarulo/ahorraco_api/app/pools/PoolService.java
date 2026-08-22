package dev.nmarulo.ahorraco_api.app.pools;

import dev.nmarulo.ahorraco_api.app.participants.ParticipantRepository;
import dev.nmarulo.ahorraco_api.app.pools.dtos.CreatePoolReq;
import dev.nmarulo.ahorraco_api.app.pools.dtos.CreatePoolRes;
import dev.nmarulo.ahorraco_api.app.pools.dtos.FindInvitationPoolRes;
import dev.nmarulo.ahorraco_api.app.pools.dtos.FindPublicIdPoolRes;
import dev.nmarulo.ahorraco_api.commons.exception.BadRequestException;
import dev.nmarulo.ahorraco_api.commons.services.AccessPoolService;
import dev.nmarulo.ahorraco_api.commons.util.BigDecimalUtils;
import dev.nmarulo.ahorraco_api.commons.util.CodeGenerator;
import dev.nmarulo.ahorraco_api.commons.util.DateUtils;
import dev.nmarulo.ahorraco_api.commons.util.IntegerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PoolService {
    
    static final int MIN_PARTICIPANTS = 2;
    
    static final int MAX_PARTICIPANTS = 30;
    
    static final int MAX_NAME_LENGTH = 80;
    
    static final int MIN_MANAGEMENT_CODE_LENGTH = 4;
    
    static final int MAX_MANAGEMENT_CODE_LENGTH = 30;
    
    static final int MAX_NOTES_LENGTH = 500;
    
    /**
     * Intentos para dar con un token de invitación libre antes de rendirse.
     */
    private static final int INVITATION_TOKEN_ATTEMPTS = 10;
    
    private static final int MIN_PAYMENT_DUE_DAY = 1;
    
    private static final int MAX_PAYMENT_DUE_DAY = 20;
    
    private final PoolRepository poolRepository;
    
    private final AccessPoolService accessPoolService;
    
    private final ParticipantRepository participantRepository;
    
    /**
     * Retornar información de una porra al organizador.
     */
    @Transactional(readOnly = true)
    public FindPublicIdPoolRes findByPublicId(final UUID publicId, final String managementCode) {
        final var pool = this.accessPoolService.getByPublicId(publicId);
        
        this.accessPoolService.requireOrganizer(pool, managementCode);
        
        return PoolMapper.toGetPoolRes(pool, this.participantRepository.countByPool(pool));
    }
    
    /**
     * Retorna información de una porra a alguien que quiere unirse a la porra.
     */
    @Transactional(readOnly = true)
    public FindInvitationPoolRes findByInvitationToken(final String invitationToken) {
        final var pool = this.accessPoolService.findByInvitationToken(invitationToken);
        
        return PoolMapper.toGetPoolInvitationRes(pool, this.participantRepository.countByPool(pool));
    }
    
    @Transactional
    public CreatePoolRes create(final CreatePoolReq request) {
        validateRequest(request);
        
        final var pool = PoolMapper.toPool(request);
        
        pool.setManagementCode(getManagementCode(request));
        pool.setInvitationToken(generateInvitationToken());
        pool.setStartDate(pool.getStartDate()
                              .withDayOfMonth(1));
        
        return PoolMapper.toCreatePoolRes(this.poolRepository.save(pool));
    }
    
    private String getManagementCode(CreatePoolReq request) {
        final var managementCodeReq = request.getManagementCode();
        
        if (StringUtils.hasText(managementCodeReq)) {
            return managementCodeReq.trim()
                                    .toUpperCase();
        }
        
        return CodeGenerator.managementCode();
    }
    
    private String generateInvitationToken() {
        for (var i = 0; i < INVITATION_TOKEN_ATTEMPTS; i++) {
            final var invitationToken = CodeGenerator.invitationToken();
            
            if (!this.poolRepository.existsByInvitationToken(invitationToken)) {
                return invitationToken;
            }
        }
        
        throw new IllegalStateException("No se ha podido generar un token de invitación libre.");
    }
    
    private void validateRequest(CreatePoolReq request) {
        final var name = request.getName();
        
        if (!StringUtils.hasText(name) || name.length() > MAX_NAME_LENGTH) {
            throw new BadRequestException("El nombre no puede pasar de %d caracteres.".formatted(MAX_NAME_LENGTH));
        }
        
        if (!BigDecimalUtils.isGreaterThanZero(request.getMonthlyFee())) {
            throw new BadRequestException("La cuota mensual tiene que ser mayor que 0.");
        }
        
        if (!IntegerUtils.isInRange(request.getNumParticipants(), MIN_PARTICIPANTS, MAX_PARTICIPANTS)) {
            throw new BadRequestException("La porra tiene que tener entre %d y %d participantes.".formatted(
                MIN_PARTICIPANTS,
                MAX_PARTICIPANTS));
        }
        
        if (!DateUtils.isDateEqualOrAfterCurrentMonth(request.getStartDate())) {
            throw new BadRequestException("El mes de inicio no puede ser menor al mes actual.");
        }
        
        if (!IntegerUtils.isInRange(request.getPaymentDueDay(), MIN_PAYMENT_DUE_DAY, MAX_PAYMENT_DUE_DAY)) {
            throw new BadRequestException("El día en que vence la cuota tiene que estar entre %d y %d.".formatted(
                MIN_PAYMENT_DUE_DAY,
                MAX_PAYMENT_DUE_DAY));
        }
        
        final var notes = request.getNotes();
        
        if (StringUtils.hasText(notes) && notes.length() > MAX_NOTES_LENGTH) {
            throw new BadRequestException("La nota para el grupo no puede pasar de %d caracteres.".formatted(
                MAX_NOTES_LENGTH));
        }
        
        final var managementCode = request.getManagementCode();
        
        if (StringUtils.hasText(managementCode) && !IntegerUtils.isInRange(managementCode.trim()
                                                                                         .length(),
                                                                           MIN_MANAGEMENT_CODE_LENGTH,
                                                                           MAX_MANAGEMENT_CODE_LENGTH)) {
            throw new BadRequestException("El código de gestión tiene que tener entre %d y %d caracteres.".formatted(
                MIN_MANAGEMENT_CODE_LENGTH,
                MAX_MANAGEMENT_CODE_LENGTH));
        }
    }
    
}
