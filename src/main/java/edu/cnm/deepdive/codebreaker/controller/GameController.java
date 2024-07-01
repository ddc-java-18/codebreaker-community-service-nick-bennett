package edu.cnm.deepdive.codebreaker.controller;

import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.service.AbstractGameService;
import edu.cnm.deepdive.codebreaker.service.AbstractUserService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
@Validated
public class GameController {

  private final AbstractGameService gameService;
  private final AbstractUserService userService;

  public GameController(AbstractGameService gameService, AbstractUserService userService) {
    this.gameService = gameService;
    this.userService = userService;
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Game post(@RequestBody @Valid Game game) {
    // TODO: 7/1/24 Return response with status 201 and Location header.
    return gameService.startGame(game, userService.getCurrentUser());
  }

  @GetMapping(path = "/{key}") // TODO: 7/1/24 Add regex for key.
  public Game get(@PathVariable UUID key) {
    return gameService.getGame(key, userService.getCurrentUser());
  }

}
