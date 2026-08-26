package be.ephec.padel_backend.repository;

import be.ephec.padel_backend.entity.Membre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembreRepository extends JpaRepository<Membre, String> {
}
