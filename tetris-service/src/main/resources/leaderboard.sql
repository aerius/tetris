DROP TABLE IF EXISTS leaderboard;

CREATE TABLE leaderboard (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  score INT NOT NULL,
  date DATE NOT NULL
);

INSERT INTO leaderboard (name, score, date) VALUES
  ('Stikstofje', 9600, PARSEDATETIME('2020-04-20', 'yyyy-MM-dd'));
