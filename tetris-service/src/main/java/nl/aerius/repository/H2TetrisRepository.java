package nl.aerius.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import nl.aerius.domain.TetrisScore;

@Repository
public class H2TetrisRepository implements TetrisRepository {
  @PersistenceContext private EntityManager entityManager;

  @Override
  @Transactional
  public List<TetrisScore> getLeaderboard() {
    return entityManager.createQuery("FROM TetrisScore "
        + "ORDER BY score DESC, date ASC", TetrisScore.class)
        .setMaxResults(10)
        .getResultList();
  }

  @Override
  @Transactional
  public void submitScore(final TetrisScore score) {
    entityManager.persist(score);
  }
}
