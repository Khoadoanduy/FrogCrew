package edu.tcu.cs.frogcrewbackend.crew;

import edu.tcu.cs.frogcrewbackend.crew.converter.CrewListDTOToCrewListConverter;
import edu.tcu.cs.frogcrewbackend.crew.converter.CrewListToCrewListDTOConverter;
import edu.tcu.cs.frogcrewbackend.crew.converter.CrewedUserDTOToCrewedUserConverter;
import edu.tcu.cs.frogcrewbackend.crew.converter.CrewedUserToCrewedUserDTOConverter;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewListDTO;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewRequestDTO;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewedUserDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.game.converter.GameDTOToGameConverter;
import edu.tcu.cs.frogcrewbackend.game.converter.GameToGameDTOConverter;
import edu.tcu.cs.frogcrewbackend.game.dto.GameDTO;
import edu.tcu.cs.frogcrewbackend.system.Result;
import edu.tcu.cs.frogcrewbackend.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class CrewListController {
    private final CrewListService crewListService;
    private final CrewListToCrewListDTOConverter crewListToCrewListDTOConverter;
    private final CrewListDTOToCrewListConverter crewListDTOToCrewListConverter;
    private final CrewedUserToCrewedUserDTOConverter crewedUserToCrewedUserDTOConverter;
    private final CrewedUserDTOToCrewedUserConverter crewedUserDTOToCrewedUserConverter;
    private final GameToGameDTOConverter gameToGameDTOConverter;
    private final GameDTOToGameConverter gameDTOToGameConverter;

    public CrewListController(CrewListService crewListService, CrewListToCrewListDTOConverter crewListToCrewListDTOConverter, CrewListDTOToCrewListConverter crewListDTOToCrewListConverter, CrewedUserToCrewedUserDTOConverter crewedUserToCrewedUserDTOConverter, CrewedUserDTOToCrewedUserConverter crewedUserDTOToCrewedUserConverter, GameToGameDTOConverter gameToGameDTOConverter, GameDTOToGameConverter gameDTOToGameConverter) {
        this.crewListService = crewListService;
        this.crewListToCrewListDTOConverter = crewListToCrewListDTOConverter;
        this.crewListDTOToCrewListConverter = crewListDTOToCrewListConverter;
        this.crewedUserToCrewedUserDTOConverter = crewedUserToCrewedUserDTOConverter;
        this.crewedUserDTOToCrewedUserConverter = crewedUserDTOToCrewedUserConverter;
        this.gameToGameDTOConverter = gameToGameDTOConverter;
        this.gameDTOToGameConverter = gameDTOToGameConverter;
    }

    @PostMapping("/crewSchedule/{gameId}")
    public Result addCrewListToGame(@PathVariable Integer gameId, @RequestBody @Valid List<CrewRequestDTO> crewRequestDTOList) {
        // Add crew users to game's crew list
        CrewList gameCrewList = this.crewListService.addCrewListToGame(gameId, crewRequestDTOList);

        // Convert CrewList to CrewListDTO to return
        CrewListDTO gameCrewListDTO = this.crewListToCrewListDTOConverter.convert(gameCrewList);

        return new Result(true, StatusCode.SUCCESS, "CrewList added to game " + gameId, gameCrewListDTO);
    }

    @GetMapping("/crewList/{gameId}")
    public Result findCrewListByGameId(@PathVariable Integer gameId) {
        CrewList crewList = this.crewListService.findCrewListByGameId(gameId);
        CrewListDTO clDTO = this.crewListToCrewListDTOConverter.convert(crewList);
        return new Result(true, StatusCode.SUCCESS, "Found crew list with gameId " + gameId, clDTO);
    }
}
