package edu.tcu.cs.frogcrewbackend.crew.converter;

import edu.tcu.cs.frogcrewbackend.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrewbackend.crew.CrewedUser;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewedUserDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.game.GameRepository;
import edu.tcu.cs.frogcrewbackend.member.Member;
import edu.tcu.cs.frogcrewbackend.member.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CrewedUserDTOToCrewedUserConverter implements Converter<CrewedUserDTO, CrewedUser> {

    @Override
    public CrewedUser convert(CrewedUserDTO source) {
        CrewedUser cu = new CrewedUser();
        cu.setCrewedUserId(source.crewedUserId());
        cu.setPosition(source.position());
        cu.setReportTime(source.reportTime());
        cu.setReportLocation(source.reportLocation());

        return cu;
    }
}
