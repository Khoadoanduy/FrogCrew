package edu.tcu.cs.frogcrewbackend.availability.converter;

import edu.tcu.cs.frogcrewbackend.availability.Availability;
import edu.tcu.cs.frogcrewbackend.availability.dto.AvailabilityDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityDTOToAvailabilityConverter implements Converter<AvailabilityDTO, Availability> {
    @Override
    public Availability convert(AvailabilityDTO source) {
        Availability memberAvailability = new Availability();
        memberAvailability.setUserId(source.userId());
        memberAvailability.setGameId(source.gameId());
        memberAvailability.setAvailability(source.availability());
        memberAvailability.setComment(source.comment());
        return memberAvailability;
    }
}
