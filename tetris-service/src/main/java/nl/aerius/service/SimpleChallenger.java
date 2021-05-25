package nl.aerius.service;

import org.springframework.stereotype.Component;

@Component
public class SimpleChallenger implements Challenger {
  @Override
  public void validate(final String token) throws ChallengeFailedException {
    if (!isValidToken(token)) {
      throw new ChallengeFailedException();
    }
  }

  private boolean isValidToken(final String challenge) {
    return challenge != null &&
    // Actual challenge check
        challenge.length() > 3;
  }
}
