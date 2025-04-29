package edu.tcu.cs.frogcrewbackend.game.converter;

import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.game.dto.GameDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameToGameDTOConverter implements Converter<Game, GameDTO> {

    private final ScheduleToScheduleDTOConverter scheduleToScheduleDTOConverter;

    public GameToGameDTOConverter(ScheduleToScheduleDTOConverter scheduleToScheduleDTOConverter) {
        this.scheduleToScheduleDTOConverter = scheduleToScheduleDTOConverter;
    }

    @Override
    public GameDTO convert(Game source) {
        GameDTO gameDTO = new GameDTO(
                source.getGameId(),
                source.getSchedule() != null ? this.scheduleToScheduleDTOConverter.convert(source.getSchedule()).scheduleId() : null,
                source.getGameDate(),
                source.getVenue(),
                source.getOpponent(),
                source.getFinalized()
        );
        return gameDTO;
    }
}
