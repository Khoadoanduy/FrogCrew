package edu.tcu.cs.frogcrewbackend.member;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class InviteService {
    public List<String> sendInvite(List<String> emails){
        for (String email : emails) {
            // send invitation
        }
        return emails;
    }
}
