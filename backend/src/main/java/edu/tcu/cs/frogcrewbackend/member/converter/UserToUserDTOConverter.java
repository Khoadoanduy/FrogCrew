package edu.tcu.cs.frogcrewbackend.member.converter;

import edu.tcu.cs.frogcrewbackend.member.Member;
import edu.tcu.cs.frogcrewbackend.member.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDTOConverter implements Converter<Member, UserDTO> {

    @Override
    public UserDTO convert(Member source) {
        return new UserDTO(
                source.getId(),
                source.getFirstName(),
                source.getLastName(),
                source.getEmail(),
                source.getPhoneNumber(),
                source.getRole(),
                source.getPositions()
        );
    }
}
