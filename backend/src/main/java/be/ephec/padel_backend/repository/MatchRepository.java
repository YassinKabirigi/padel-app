package be.ephec.padel_backend.repository;

import be.ephec.padel_backend.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Integer> {
}
