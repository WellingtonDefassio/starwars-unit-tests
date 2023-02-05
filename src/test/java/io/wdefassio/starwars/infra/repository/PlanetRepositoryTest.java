package io.wdefassio.starwars.infra.repository;

import io.wdefassio.starwars.domain.Planet;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static io.wdefassio.starwars.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository planetRepository;
    @Autowired
    private TestEntityManager testEntityManager;

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
        testEntityManager.persistFlushFind(PLANET);
        Planet sameName = new Planet(null, "name", "climate", "terrains");
        assertThatThrownBy(() -> planetRepository.save(sameName)).isInstanceOf(RuntimeException.class);

    }


}
