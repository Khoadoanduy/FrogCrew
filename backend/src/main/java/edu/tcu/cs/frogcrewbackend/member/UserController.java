package edu.tcu.cs.frogcrewbackend.member;

import edu.tcu.cs.frogcrewbackend.member.converter.UserDTOToUserConverter;
import edu.tcu.cs.frogcrewbackend.member.converter.UserToUserDTOConverter;
import edu.tcu.cs.frogcrewbackend.member.dto.UserDTO;
import edu.tcu.cs.frogcrewbackend.system.Result;
import edu.tcu.cs.frogcrewbackend.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/crewMember")
public class UserController {
    private final UserService userService;
    private final UserToUserDTOConverter userToUserDTOConverter;
    private final UserDTOToUserConverter userDTOToUserConverter;

    public UserController(UserService userService, UserToUserDTOConverter userToUserDTOConverter, UserDTOToUserConverter userDTOToUserConverter) {
        this.userService = userService;
        this.userToUserDTOConverter = userToUserDTOConverter;
        this.userDTOToUserConverter = userDTOToUserConverter;
    }

    @PostMapping
    public Result createMember(@RequestBody @Valid Member newMember) {
        Member savedMember = this.userService.createMember(newMember);
        UserDTO savedMemberDTO = this.userToUserDTOConverter.convert(savedMember);
        return new Result(true, StatusCode.SUCCESS, "Member created", savedMemberDTO);
    }

    @GetMapping
    public Result findAllMembers() {
        List<Member> foundMembers = this.userService.findAllMembers();

        // Convert foundMembers to a list of UserDTO
        List<UserDTO> userDTOS = foundMembers.stream()
                .map(this.userToUserDTOConverter::convert)
                .collect(Collectors.toList());

        // no password field
        return new Result(true, StatusCode.SUCCESS, "Found all members", userDTOS);
    }

    @GetMapping("/{userId}")
    public Result findMemberById(@PathVariable Integer userId) {
        Member foundMember = this.userService.findMemberById(userId);
        UserDTO userDTO = this.userToUserDTOConverter.convert(foundMember);
        return new Result(true, StatusCode.SUCCESS, "Found member with Id: " + userId, userDTO);
    }

//    @PutMapping("/{userId}")
//    public Result updateMember(@PathVariable Integer userId, @RequestBody @Valid UserDTO userDTO) {
//        Member update = this.userDTOToUserConverter.convert(userDTO);
//        Member updatedMember = this.userService.updateMember(userId, update);
//        UserDTO savedUserDTO = this.userToUserDTOConverter.convert(updatedMember);
//        return new Result(true, StatusCode.SUCCESS, "Member updated with Id: " + userId, savedUserDTO);
//    }

    @DeleteMapping("/{userId}")
    public Result deleteMember(@PathVariable Integer userId) {
        this.userService.deleteMember(userId);
        return new Result(true, StatusCode.SUCCESS, "Member deleted with Id: " + userId);
    }
}
