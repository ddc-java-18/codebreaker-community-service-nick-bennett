package edu.cnm.deepdive.codebreaker.service;

import edu.cnm.deepdive.codebreaker.model.dao.GameRepository;
import edu.cnm.deepdive.codebreaker.model.dao.GuessRepository;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import java.util.UUID;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService implements AbstractGameService {

  private final GameRepository gameRepository;
  private final GuessRepository guessRepository;
  private final RandomGenerator rng;

  @Autowired
  public GameService(
      GameRepository gameRepository, GuessRepository guessRepository, RandomGenerator rng) {
    this.gameRepository = gameRepository;
    this.guessRepository = guessRepository;
    this.rng = rng;
  }

  @Override
  public Game startGame(Game game, User user) throws InvalidPoolException {
    game.setPool(validatePool(game.getPool()));
    game.setPlayer(user);
    game.setSecretCode(generateSecretCode(game.getPool(), game.getCodeLength()));
    return gameRepository.save(game);
  }

  @Override
  public Game getGame(UUID gameKey, User user) {
    return gameRepository
        .getByExternalKeyAndPlayer(gameKey, user)
        .orElseThrow();
  }

  @Override
  public Guess submitGuess(UUID gameKey, Guess guess, User user) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Guess getGuess(UUID gameKey, UUID guessKey, User user) {
//    return gameRepository
//        .getByExternalKeyAndPlayer(gameKey, user)
//        .flatMap((game) -> guessRepository.getByExternalKeyAndGame(guessKey, game))
//        .orElseThrow();
    return guessRepository
        .getByExternalKeyAndGameExternalKeyAndGamePlayer(guessKey, gameKey, user)
        .orElseThrow();
  }

  private static String validatePool(String pool) throws InvalidPoolException {
    return pool
        .codePoints()
        .peek(GameService::validateCodePoint)
        .distinct()
        .sorted()
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  private static int[] codePoints(String input) {
    return input
        .codePoints()
        .toArray();
  }

  private static void validateCodePoint(int codePoint) throws InvalidPoolException {
    if (
        Character.isWhitespace(codePoint)
        || Character.isISOControl(codePoint)
        || !Character.isDefined(codePoint)
    ) {
      throw new InvalidPoolException();
    }
  }

  private String generateSecretCode(String pool, int codeLength) {
    int[] poolCodePoints = codePoints(pool);
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < codeLength; i++) {
      builder.appendCodePoint(poolCodePoints[rng.nextInt(poolCodePoints.length)]);
    }
    return builder.toString();
  }

  public static class InvalidPoolException extends IllegalArgumentException {

    public InvalidPoolException() {
    }

    public InvalidPoolException(String message) {
      super(message);
    }

    public InvalidPoolException(String message, Throwable cause) {
      super(message, cause);
    }

    public InvalidPoolException(Throwable cause) {
      super(cause);
    }
  }

}
