package edu.tcu.cs.frogcrewbackend.member;

import edu.tcu.cs.frogcrewbackend.member.dto.InviteDTO;
import edu.tcu.cs.frogcrewbackend.system.Result;
import edu.tcu.cs.frogcrewbackend.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/invite")
public class InviteController {
    private final InviteService inviteService;

    public InviteController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @PostMapping
    public Result inviteMember(@RequestBody @Valid InviteDTO invite) {
        this.inviteService.sendInvite(invite.emails());
        return new Result(true, StatusCode.SUCCESS, "Invite success", invite);
    }
}
