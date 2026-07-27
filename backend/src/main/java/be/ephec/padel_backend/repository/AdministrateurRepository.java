package be.ephec.padel_backend.repository;

import be.ephec.padel_backend.entity.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrateurRepository extends JpaRepository<Administrateur, Integer> {
}