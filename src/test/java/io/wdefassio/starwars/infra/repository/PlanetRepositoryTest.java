package io.wdefassio.starwars.infra.repository;

import io.wdefassio.starwars.application.service.util.QueryBuilder;
import io.wdefassio.starwars.domain.Planet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static io.wdefassio.starwars.common.PlanetConstants.PLANET;
import static io.wdefassio.starwars.common.PlanetConstants.TATTOINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository planetRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    private void afterEach() {
        PLANET.setId(null);
    }

    @Test
    @DisplayName("should be able to create a planet with valid data")
    public void createPlanetSuccess() {
        Planet save = planetRepository.save(PLANET);
        Planet planet = testEntityManager.find(Planet.class, save.getId());

        assertThat(planet).isNotNull();
        assertThat(planet.getName()).isEqualTo(save.getName());
        assertThat(planet.getClimate()).isEqualTo(save.getClimate());
        assertThat(planet.getTerrain()).isEqualTo(save.getTerrain());

    }

    @Test
    @DisplayName("should throws when a planet is create with a invalid data")
    public void createPlanetWithInvalidData() {
        Planet emptyPlanet = new Planet(null, null, null, null);
        Planet invalidPlanet = new Planet(null, "", "", "");

        assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> planetRepository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);

    }

    @Test
    @DisplayName("should throws when a planet is create with a existent name")
    public void createPlanetExistingName() {
        testEntityManager.persistFlushFind(PLANET);
        Planet sameName = new Planet(null, "name", "climate", "terrains");
        assertThatThrownBy(() -> planetRepository.save(sameName)).isInstanceOf(RuntimeException.class);

    }

    @Test
    @DisplayName("should be able to find a planet with a correct id")
    public void getPlanetByIdSuccess() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> optionalPlanet = planetRepository.findById(planet.getId());

        assertThat(optionalPlanet).isNotEmpty();
        assertThat(optionalPlanet.get()).isEqualTo(planet);

    }

    @Test
    @DisplayName("should return a empty optional when planet not exists")
    public void getPlanetByIdEmpty() {

        Optional<Planet> optionalPlanet = planetRepository.findById(1L);
        assertThat(optionalPlanet).isEmpty();
    }

    @Test
    @DisplayName("should be able to find a planet with a correct name")
    public void getPlanetByNameSuccess() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> optionalPlanet = planetRepository.findByName(planet.getName());

        assertThat(optionalPlanet).isNotEmpty();
        assertThat(optionalPlanet.get()).isEqualTo(planet);

    }

    @Test
    @DisplayName("should return a empty optional when planet not exists")
    public void getPlanetByNameEmpty() {

        Optional<Planet> optionalPlanet = planetRepository.findByName(PLANET.getName());
        assertThat(optionalPlanet).isEmpty();
    }

    @Test
    @Sql(scripts = "/create-planets.sql")
    @DisplayName("should return planets by filter")
    public void getPlanetsByFilter() {
        Example<Planet> queryWithOutFilters = QueryBuilder.makeQuery(new Planet());
        Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(null, null, TATTOINE.getClimate(), TATTOINE.getTerrain()));

        List<Planet> responseWithOut = planetRepository.findAll(queryWithOutFilters);
        List<Planet> responseFilter = planetRepository.findAll(queryWithFilters);

        assertThat(responseWithOut).isNotEmpty();
        assertThat(responseWithOut).hasSize(3);
        assertThat(responseFilter).isNotEmpty();
        assertThat(responseFilter).hasSize(1);
        assertThat(responseFilter.get(0)).isEqualTo(TATTOINE);
    }

    @Test
    @DisplayName("should return a empty array when no filter is find")
    public void listPlanetsEmpty() {

        Example<Planet> planetExample = QueryBuilder.makeQuery(new Planet());
        List<Planet> response = planetRepository.findAll(planetExample);

        assertThat(response).isEmpty();

    }
    @Test
    @DisplayName("should be able to delete a planet by id")
    public void deletePlanetSuccess() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        planetRepository.deleteById(planet.getId());

        Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());
        assertThat(removedPlanet).isNull();

    }

    @Test
    @DisplayName("should throws when a invalid id is provided")
    public void deletePlanetThrows() {
        assertThatThrownBy(() -> planetRepository.deleteById(1L)).isInstanceOf(EmptyResultDataAccessException.class);

    }


}
