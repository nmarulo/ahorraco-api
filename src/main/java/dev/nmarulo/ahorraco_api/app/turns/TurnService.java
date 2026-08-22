package dev.nmarulo.ahorraco_api.app.turns;

import dev.nmarulo.ahorraco_api.app.participants.Participant;
import dev.nmarulo.ahorraco_api.app.participants.ParticipantRepository;
import dev.nmarulo.ahorraco_api.app.pools.Pool;
import dev.nmarulo.ahorraco_api.app.turns.dtos.CreateDrawReq;
import dev.nmarulo.ahorraco_api.app.turns.dtos.CreateDrawRes;
import dev.nmarulo.ahorraco_api.commons.exception.BadRequestException;
import dev.nmarulo.ahorraco_api.commons.services.AccessPoolService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TurnService {
    
    private static final SecureRandom RANDOM = new SecureRandom();
    
    private final TurnRepository turnRepository;
    
    private final ParticipantRepository participantRepository;
    
    private final AccessPoolService accessPoolService;
    
    /**
     * Sorteo del orden de cobro.
     */
    @Transactional
    public CreateDrawRes createDraw(final UUID poolPublicId, final String managementCode, final CreateDrawReq request) {
        final var pool = this.accessPoolService.getByPublicId(poolPublicId);
        
        this.accessPoolService.requireOrganizer(pool, managementCode);
        
        checkDrawIsNotDone(pool);
        
        final var participants = this.participantRepository.findAllByPoolOrderByIdAsc(pool);
        
        checkPoolIsFull(pool, participants);
        
        final var organizer = findOrganizer(request, participants);
        final var shuffleOrder = shuffleOrder(participants, organizer);
        final var turns = buildTurns(pool, shuffleOrder, organizer);
        
        return TurnMapper.toCreateDrawRes(this.turnRepository.saveAll(turns));
    }
    
    private void checkDrawIsNotDone(final Pool pool) {
        if (this.turnRepository.existsByPool(pool)) {
            throw new BadRequestException("El sorteo de esta porra ya está hecho.");
        }
    }
    
    private void checkPoolIsFull(final Pool pool, final List<Participant> participants) {
        final var numParticipants = pool.getNumParticipants();
        final var participantCount = participants.size();
        
        if (participantCount < numParticipants) {
            throw new BadRequestException("Todavía falta gente por unirse: hay %d de %d.".formatted(participantCount,
                                                                                                    numParticipants));
        }
    }
    
    private Participant findOrganizer(final CreateDrawReq request, final List<Participant> participants) {
        if (!Boolean.TRUE.equals(request.getOrganizerFirst())) {
            return null;
        }
        
        if (StringUtils.isBlank(request.getOrganizerPublicId())) {
            throw new BadRequestException(
                "Para reservar el primer turno hay que indicar cuál de los participantes es el organizador.");
        }
        
        return participants.stream()
                           .filter(participant -> Strings.CS.equals(participant.getPublicId()
                                                                               .toString(),
                                                                    request.getOrganizerPublicId()))
                           .findFirst()
                           .orElseThrow(() -> new BadRequestException(
                               "El organizador no existe en la lista de participantes."));
    }
    
    /**
     * Baraja a los participantes, dejando fuera del sorteo al organizador si se reserva el primer
     * turno.
     */
    private List<Participant> shuffleOrder(final List<Participant> participants, final Participant organizer) {
        final var resultList = new ArrayList<>(participants);
        
        if (organizer == null) {
            Collections.shuffle(resultList, RANDOM);
            
            return resultList;
        }
        
        resultList.removeIf(value -> Objects.equals(value.getId(), organizer.getId()));
        
        Collections.shuffle(resultList, RANDOM);
        
        resultList.addFirst(organizer);
        
        return resultList;
    }
    
    private List<Turn> buildTurns(final Pool pool, final List<Participant> ordered, final Participant organizer) {
        final var turns = new ArrayList<Turn>(ordered.size());
        
        for (var index = 0; index < ordered.size(); index++) {
            final var turn = new Turn();
            final var participant = ordered.get(index);
            
            turn.setPool(pool);
            turn.setParticipant(participant);
            turn.setPosition(index + 1);
            turn.setMonth(pool.getStartDate()
                              .plusMonths(index));
            turn.setPinned(organizer != null && Objects.equals(organizer.getId(), participant.getId()));
            
            turns.add(turn);
        }
        
        return turns;
    }
    
}
