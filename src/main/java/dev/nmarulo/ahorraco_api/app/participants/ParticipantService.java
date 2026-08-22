package dev.nmarulo.ahorraco_api.app.participants;

import dev.nmarulo.ahorraco_api.app.participants.dtos.FindAllParticipantRes;
import dev.nmarulo.ahorraco_api.app.participants.dtos.JoinPoolReq;
import dev.nmarulo.ahorraco_api.app.participants.dtos.JoinPoolRes;
import dev.nmarulo.ahorraco_api.app.pools.Pool;
import dev.nmarulo.ahorraco_api.commons.exception.BadRequestException;
import dev.nmarulo.ahorraco_api.commons.exception.UnauthorizedException;
import dev.nmarulo.ahorraco_api.commons.services.AccessPoolService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Unirse a una porra por el enlace y ver quién está dentro (I-02).
 */
@Service
@RequiredArgsConstructor
public class ParticipantService {
    
    static final int MAX_FULL_NAME_LENGTH = 80;
    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[\\d\\s.-]{6,20}$");
    
    private final ParticipantRepository participantRepository;
    
    private final AccessPoolService accessPoolService;
    
    @Transactional
    public JoinPoolRes join(final UUID poolPublicId, final JoinPoolReq request) {
        validateRequest(request);
        
        final var pool = this.accessPoolService.getByPublicId(poolPublicId);
        final var fullName = request.getFullName()
                                    .trim();
        
        checkInvitationToken(pool, request.getInvitationToken());
        checkThereIsRoom(pool);
        checkNameIsFree(pool, fullName);
        
        final var participant = ParticipantMapper.toParticipant(request);
        
        participant.setPool(pool);
        participant.setFullName(fullName);
        participant.setPhone(StringUtils.trimToNull(request.getPhone()));
        
        return ParticipantMapper.toJoinPoolRes(this.participantRepository.save(participant));
    }
    
    @Transactional(readOnly = true)
    public FindAllParticipantRes findAll(final UUID poolPublicId, final String managementCode) {
        final var pool = this.accessPoolService.getByPublicId(poolPublicId);
        final var organizer = this.accessPoolService.isOrganizer(pool, managementCode);
        final Function<Participant, FindAllParticipantRes.ParticipantRes> toResponse;
        
        if (organizer) {
            toResponse = participant -> ParticipantMapper.toParticipantRes(participant, false);
        } else {
            toResponse = participant -> ParticipantMapper.toParticipantRes(participant, true);
        }
        
        final var participantResList = this.participantRepository.findAllByPoolOrderByIdAsc(pool)
                                                                 .stream()
                                                                 .map(toResponse)
                                                                 .toList();
        
        return new FindAllParticipantRes(participantResList);
    }
    
    /**
     * Comprueba que el token del enlace es el de esa porra.
     */
    private void checkInvitationToken(final Pool pool, final String invitationToken) {
        final var invitationTrim = StringUtils.trimToEmpty(invitationToken);
        
        if (Strings.CI.equals(pool.getInvitationToken(), invitationTrim)) {
            return;
        }
        
        throw new UnauthorizedException("Este enlace de invitación no vale o ya no existe.");
    }
    
    private void validateRequest(JoinPoolReq request) {
        final var fullNameTrim = StringUtils.trim(request.getFullName());
        
        if (StringUtils.isBlank(fullNameTrim)) {
            throw new BadRequestException("El nombre no puede estar vacío.");
        }
        
        if (StringUtils.length(fullNameTrim) > MAX_FULL_NAME_LENGTH) {
            throw new BadRequestException("El nombre no puede pasar de %d caracteres.".formatted(MAX_FULL_NAME_LENGTH));
        }
        
        final var phone = StringUtils.trim(request.getPhone());
        
        if (StringUtils.isNotBlank(phone) && !PHONE_PATTERN.matcher(phone)
                                                           .matches()) {
            throw new BadRequestException("El teléfono no tiene un formato válido.");
        }
        
        if (StringUtils.isBlank(request.getInvitationToken())) {
            throw new BadRequestException("El token de invitación no puede estar vacío.");
        }
    }
    
    private void checkThereIsRoom(final Pool pool) {
        if (this.participantRepository.countByPool(pool) >= pool.getNumParticipants()) {
            throw new BadRequestException("Esta porra ya está completa.");
        }
    }
    
    private void checkNameIsFree(final Pool pool, final String fullName) {
        if (this.participantRepository.existsByPoolAndFullNameIgnoreCase(pool, fullName)) {
            throw new BadRequestException(
                "Ya hay alguien en la porra con ese nombre. Añade el apellido para distinguiros.");
        }
    }
    
}
