package edu.tcu.cs.frogcrewbackend.game.converter;

import edu.tcu.cs.frogcrewbackend.game.GameSchedule;
import edu.tcu.cs.frogcrewbackend.game.dto.GameScheduleDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ScheduleDTOToScheduleConverter implements Converter<GameScheduleDTO, GameSchedule> {
    @Override
    public GameSchedule convert(GameScheduleDTO source) {
        GameSchedule gameSchedule = new GameSchedule();
        gameSchedule.setId(source.scheduleId());
        gameSchedule.setSport(source.sport());
        gameSchedule.setSeason(source.season());
        return gameSchedule;
    }
}
