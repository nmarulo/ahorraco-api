package dev.nmarulo.ahorraco_api.app.turns;

import dev.nmarulo.ahorraco_api.app.turns.dtos.CreateDrawRes;
import dev.nmarulo.ahorraco_api.app.turns.dtos.FindOrderRes;

import java.util.List;

public final class TurnMapper {
    
    private TurnMapper() {
    }
    
    public static CreateDrawRes toCreateDrawRes(final List<Turn> turns) {
        final var turnResList = turns.stream()
                                     .map(TurnMapper::toTurnRes)
                                     .toList();
        
        return new CreateDrawRes(turnResList);
    }
    
    public static CreateDrawRes.TurnRes toTurnRes(final Turn turn) {
        final var participant = turn.getParticipant();
        final var participantPublicId = participant.getPublicId()
                                                   .toString();
        
        return new CreateDrawRes.TurnRes(turn.getPosition(),
                                         participantPublicId,
                                         participant.getFullName(),
                                         turn.getMonth(),
                                         turn.isPinned());
    }
    
    public static FindOrderRes.TurnRes toOrderTurnRes(final Turn turn) {
        final var participant = turn.getParticipant();
        final var participantPublicId = participant.getPublicId()
                                                   .toString();
        
        return new FindOrderRes.TurnRes(turn.getPosition(),
                                        participantPublicId,
                                        participant.getFullName(),
                                        turn.getMonth(),
                                        turn.isPinned());
    }
    
}
