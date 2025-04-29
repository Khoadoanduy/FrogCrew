package edu.tcu.cs.frogcrewbackend.crew;

import edu.tcu.cs.frogcrewbackend.crew.converter.CrewedUserToCrewedUserDTOConverter;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewedUserDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.game.GameRepository;
import edu.tcu.cs.frogcrewbackend.system.Result;
import edu.tcu.cs.frogcrewbackend.system.StatusCode;
import edu.tcu.cs.frogcrewbackend.system.exception.ObjectNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/crewedUser")
public class CrewedUserController {
    private final CrewedUserService crewedUserService;
    private final CrewedUserToCrewedUserDTOConverter crewedUserToCrewedUserDTOConverter;

    public CrewedUserController(CrewedUserService crewedUserService, GameRepository gameRepository, CrewedUserToCrewedUserDTOConverter crewedUserToCrewedUserDTOConverter) {
        this.crewedUserService = crewedUserService;
        this.crewedUserToCrewedUserDTOConverter = crewedUserToCrewedUserDTOConverter;
    }

    @GetMapping("/{gameId}/{position}")
    public Result findCrewedUsersByGameAndPosition(@PathVariable Integer gameId, @PathVariable String position) {
        List<CrewedUser> crewedUsers = this.crewedUserService.findCrewedUsersByGameAndPosition(gameId, position);
        // convert to dtos for result
        List<CrewedUserDTO> crewedUserDTOS = crewedUsers.stream()
                .map(this.crewedUserToCrewedUserDTOConverter::convert)
                .collect(Collectors.toList());

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Found crewedUsers with gameId " + gameId + " and position " + position,
                crewedUserDTOS);
    }
}
