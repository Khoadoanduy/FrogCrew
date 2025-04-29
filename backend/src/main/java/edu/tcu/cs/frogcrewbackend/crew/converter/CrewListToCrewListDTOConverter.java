package edu.tcu.cs.frogcrewbackend.crew.converter;

import edu.tcu.cs.frogcrewbackend.crew.CrewList;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewListDTO;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewedUserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CrewListToCrewListDTOConverter implements Converter <CrewList, CrewListDTO> {
    private final CrewedUserToCrewedUserDTOConverter crewedUserToCrewedUserDTOConverter;

    public CrewListToCrewListDTOConverter(CrewedUserToCrewedUserDTOConverter crewedUserToCrewedUserDTOConverter) {
        this.crewedUserToCrewedUserDTOConverter = crewedUserToCrewedUserDTOConverter;
    }

    @Override
    public CrewListDTO convert(CrewList source) {
        List<CrewedUserDTO> crewedUserDTOS = source.getCrewedUsers()
                .stream()
                .map(crewedUserToCrewedUserDTOConverter::convert)
                .collect(Collectors.toList());

        return new CrewListDTO(
                source.getGame().getGameId(),
                source.getGame().getGameDate(),
                source.getGame().getVenue(),
                source.getGame().getOpponent(),
                crewedUserDTOS
        );
    }
}
