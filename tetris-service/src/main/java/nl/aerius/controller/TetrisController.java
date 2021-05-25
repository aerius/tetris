package nl.aerius.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import nl.aerius.domain.TetrisScore;
import nl.aerius.repository.TetrisRepository;

@Controller
public class TetrisController {
  @Autowired TetrisRepository repository;

  @GetMapping(value = { "/leaderboard", "/" })
  public String leaderboard(final Model model) {
    final List<TetrisScore> leaderboard = repository.getLeaderboard();
    model.addAttribute("leaderboard", leaderboard);
    return "leaderboard";
  }
}
