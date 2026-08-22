package dev.nmarulo.ahorraco_api.app.participants;

import dev.nmarulo.ahorraco_api.app.participants.dtos.FindAllParticipantRes;
import dev.nmarulo.ahorraco_api.app.participants.dtos.JoinPoolReq;
import dev.nmarulo.ahorraco_api.app.participants.dtos.JoinPoolRes;

public final class ParticipantMapper {
    
    private ParticipantMapper() {
    }
    
    public static Participant toParticipant(JoinPoolReq request) {
        final var participant = new Participant();

        participant.setFullName(request.getFullName());
        participant.setPhone(request.getPhone());

        return participant;
    }

    public static JoinPoolRes toJoinPoolRes(final Participant participant) {
        final var publicId = participant.getPublicId()
                                        .toString();

        return new JoinPoolRes(publicId);
    }
    
    public static FindAllParticipantRes.ParticipantRes toParticipantRes(final Participant participant,
                                                                        boolean excludePhone) {
        final var publicId = participant.getPublicId()
                                        .toString();
        final var phone = excludePhone ? null : participant.getPhone();
        
        return new FindAllParticipantRes.ParticipantRes(publicId, participant.getFullName(), phone);
    }
    
}
