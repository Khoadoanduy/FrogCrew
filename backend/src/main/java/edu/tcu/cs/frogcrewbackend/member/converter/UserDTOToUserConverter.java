package edu.tcu.cs.frogcrewbackend.member.converter;

import edu.tcu.cs.frogcrewbackend.member.Member;
import edu.tcu.cs.frogcrewbackend.member.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDTOToUserConverter implements Converter<UserDTO, Member> {

    @Override
    public Member convert(UserDTO source) {
        Member member = new Member();
        member.setFirstName(source.firstName());
        member.setLastName(source.lastName());
        member.setEmail(source.email());
        member.setPhoneNumber(source.phoneNumber());
        member.setRole(source.role());
        member.setPositions(source.positions());
        return member;
    }
}
