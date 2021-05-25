package nl.aerius.service;

public interface Challenger {
  void validate(String challenge) throws ChallengeFailedException;
}
