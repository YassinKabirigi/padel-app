package be.ephec.padel_backend.repository;

import be.ephec.padel_backend.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementRepository extends JpaRepository<Paiement, Integer> {
}
