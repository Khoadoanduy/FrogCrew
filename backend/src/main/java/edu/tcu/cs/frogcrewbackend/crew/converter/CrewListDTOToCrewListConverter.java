package edu.tcu.cs.frogcrewbackend.crew.converter;

import edu.tcu.cs.frogcrewbackend.crew.CrewList;
import edu.tcu.cs.frogcrewbackend.crew.CrewedUser;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewListDTO;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewedUserDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.game.GameRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CrewListDTOToCrewListConverter implements Converter<CrewListDTO, CrewList> {

    private final GameRepository gameRepository;
    private final CrewedUserDTOToCrewedUserConverter crewedUserConverter;

    public CrewListDTOToCrewListConverter(GameRepository gameRepository, CrewedUserDTOToCrewedUserConverter crewedUserConverter) {
        this.gameRepository = gameRepository;
        this.crewedUserConverter = crewedUserConverter;
    }

    @Override
    public CrewList convert(CrewListDTO source) {
        CrewList cl = new CrewList();

        // set game
        Game game = gameRepository.findById(source.gameId())
                .orElseThrow(() -> new ObjectNotFoundException("Game", source.gameId()));
        cl.setGame(game);

        // set crewedUsers
        List<CrewedUser> crewedUsers = source.crewedUserDTOS().stream()
                .map(crewedUserConverter::convert)
                .collect(Collectors.toList());
        cl.setCrewedUsers(crewedUsers);

        return cl;
    }
}
