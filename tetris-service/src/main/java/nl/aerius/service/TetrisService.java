package nl.aerius.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.aerius.domain.TetrisScore;
import nl.aerius.repository.TetrisRepository;
import nl.aerius.util.TetrisUtil;

@CrossOrigin
@RestController
public class TetrisService {
  private static final Logger LOG = LoggerFactory.getLogger(TetrisService.class);

  @Autowired TetrisRepository tetrisRepository;

  @Autowired Challenger challenger;

  @GetMapping(value = "/api/leaderboard")
  public List<TetrisScore> retrieveLeaderboard() {
    return tetrisRepository.getLeaderboard();
  }

  @PostMapping(value = "/api/submit")
  public List<TetrisScore> submitScore(final String name, final int[] score, final int clientScore, final String token)
      throws ChallengeFailedException {
    final int scoreNum = TetrisUtil.calculateScore(score);

    // Sanity check for the client's score calculation
    if (clientScore != scoreNum) {
      throw new ChallengeFailedException();
    }

    LOG.info("Submitting score {} {} ({}) {}", name, score, scoreNum, token);

    challenger.validate(token);

    final TetrisScore tetrisScore = new TetrisScore();
    tetrisScore.setName(name);
    tetrisScore.setScore(scoreNum);
    tetrisScore.setLines(Arrays.stream(score)
        .mapToObj(v -> String.valueOf(v))
        .collect(Collectors.joining(",")));
    tetrisScore.setDate(new Date());

    tetrisRepository.submitScore(tetrisScore);

    return tetrisRepository.getLeaderboard();
  }
}
