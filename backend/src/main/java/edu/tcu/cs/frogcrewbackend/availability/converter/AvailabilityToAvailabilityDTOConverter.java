package edu.tcu.cs.frogcrewbackend.availability.converter;

import edu.tcu.cs.frogcrewbackend.availability.Availability;
import edu.tcu.cs.frogcrewbackend.availability.dto.AvailabilityDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityToAvailabilityDTOConverter implements Converter<Availability, AvailabilityDTO> {

    @Override
    public AvailabilityDTO convert(Availability source) {
        return new AvailabilityDTO(
                source.getUserId(),
                source.getGameId(),
                source.getAvailability(),
                source.getComment()
        );
    }
}
