package io.wdefassio.starwars.infra.repository;

import io.wdefassio.starwars.domain.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
    Optional<Planet> findByName(String name);
}
