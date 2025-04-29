package edu.tcu.cs.frogcrewbackend.game.converter;

import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.game.dto.GameDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameDTOToGameConverter implements Converter<GameDTO, Game> {
    @Override
    public Game convert(GameDTO source) {
        Game game = new Game();
        game.setGameId(source.gameId());
        game.setGameDate(source.gameDate());
        game.setVenue(source.venue());
        game.setOpponent(source.opponent());
        game.setFinalized(source.isFinalized());
        return game;
    }
}

