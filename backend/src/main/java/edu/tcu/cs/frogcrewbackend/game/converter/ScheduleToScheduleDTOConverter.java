package edu.tcu.cs.frogcrewbackend.game.converter;

import edu.tcu.cs.frogcrewbackend.game.GameSchedule;
import edu.tcu.cs.frogcrewbackend.game.dto.GameScheduleDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ScheduleToScheduleDTOConverter implements Converter <GameSchedule, GameScheduleDTO>{

    @Override
    public GameScheduleDTO convert(GameSchedule source) {
        GameScheduleDTO scheduleDTO = new GameScheduleDTO(
                source.getId(),
                source.getSport(),
                source.getSeason());
        return scheduleDTO;
    }
}
