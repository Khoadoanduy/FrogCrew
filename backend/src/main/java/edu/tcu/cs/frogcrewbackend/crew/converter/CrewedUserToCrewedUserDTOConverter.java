package edu.tcu.cs.frogcrewbackend.crew.converter;

import edu.tcu.cs.frogcrewbackend.crew.CrewedUser;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewedUserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CrewedUserToCrewedUserDTOConverter implements Converter<CrewedUser, CrewedUserDTO> {
    @Override
    public CrewedUserDTO convert(CrewedUser source) {
        return new CrewedUserDTO(
                source.getCrewedUserId(),
                source.getUser().getId(),
                source.getGame().getGameId(),
                source.getUser().getFirstName() + " " + source.getUser().getLastName(),
                source.getPosition(),
                source.getReportTime(),
                source.getReportLocation()
        );
    }
}
