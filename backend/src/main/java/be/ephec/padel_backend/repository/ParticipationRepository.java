package be.ephec.padel_backend.repository;

import be.ephec.padel_backend.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
}