package nl.aerius.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ChallengeFailedException extends Exception {
  private static final long serialVersionUID = 1L;
}
