package be.ephec.padel_backend.repository;

import be.ephec.padel_backend.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
    List<Participation> findByMatch_IdMatch(Integer idMatch);
    Optional<Participation> findByMatch_IdMatchAndMembre_Matricule(Integer idMatch, String matricule);
}
