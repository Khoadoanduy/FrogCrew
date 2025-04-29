package edu.tcu.cs.frogcrewbackend.availability;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AvailabilityService {
    private final AvailabilityRepository availRepository;

    public AvailabilityService(AvailabilityRepository availRepository) {
        this.availRepository = availRepository;
    }

    public Availability addAvailability(Availability avail) {
        return this.availRepository.save(avail);
    }
}
