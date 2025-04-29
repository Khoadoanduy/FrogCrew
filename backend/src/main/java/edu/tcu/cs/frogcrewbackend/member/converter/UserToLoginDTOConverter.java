package edu.tcu.cs.frogcrewbackend.member.converter;

import edu.tcu.cs.frogcrewbackend.member.Member;
import edu.tcu.cs.frogcrewbackend.member.dto.LoginDTO;
import edu.tcu.cs.frogcrewbackend.member.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToLoginDTOConverter implements Converter<Member, LoginDTO> {
    @Override
    public LoginDTO convert(Member source) {
        return new LoginDTO(source.getId(), source.getRole(), null);
    }
}
