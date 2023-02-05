package io.wdefassio.starwars.application.service;

import io.wdefassio.starwars.application.service.util.QueryBuilder;
import io.wdefassio.starwars.domain.Planet;
import io.wdefassio.starwars.infra.repository.PlanetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanetServiceImp implements PlanetService {
    private final PlanetRepository planetRepository;

    @Override
    public Planet create(Planet planet) {
        return planetRepository.save(planet);
    }

    @Override
    public Optional<Planet> get(Long id) {
        return planetRepository.findById(id);
    }

    @Override
    public Optional<Planet> getByName(String name) {
        return planetRepository.findByName(name);
    }

    @Override
    public List<Planet> list(String terrain, String climate) {
        Planet findPlanet = new Planet(null, null, climate, terrain);
        Example<Planet> planetExample = QueryBuilder.makeQuery(findPlanet);
        return planetRepository.findAll(planetExample);
    }

    @Override
    public void remove(Long id) {
        planetRepository.deleteById(id);
    }
}
