package io.wdefassio.starwars.application.service;

import io.wdefassio.starwars.domain.Planet;
import net.bytebuddy.jar.asm.commons.Remapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface PlanetService {

    Planet create(Planet planet);
    Optional<Planet> get(Long id);
    Optional<Planet> getByName(String name);
    List<Planet> list(String terrain, String climate);
    void remove(Long id);
}
