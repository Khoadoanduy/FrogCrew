package edu.tcu.cs.frogcrewbackend.security;

import edu.tcu.cs.frogcrewbackend.member.Member;
import edu.tcu.cs.frogcrewbackend.member.MyUserPrincipal;
import edu.tcu.cs.frogcrewbackend.member.converter.UserToLoginDTOConverter;
import edu.tcu.cs.frogcrewbackend.member.converter.UserToUserDTOConverter;
import edu.tcu.cs.frogcrewbackend.member.dto.LoginDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserToLoginDTOConverter userToLoginDTOConverter;

    public AuthService(JwtProvider jwtProvider, UserToLoginDTOConverter userToLoginDTOConverter) {
        this.jwtProvider = jwtProvider;
        this.userToLoginDTOConverter = userToLoginDTOConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // user info
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Member member = principal.getMember();
        LoginDTO loginDTO = userToLoginDTOConverter.convert(member);

        // jwt
        String token = this.jwtProvider.createToken(authentication);
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("userId", loginDTO.userId());
        loginInfo.put("role", loginDTO.role());
        loginInfo.put("token", token);

        return loginInfo;
    }
}
