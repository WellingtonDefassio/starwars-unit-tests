package io.wdefassio.starwars;

import io.wdefassio.starwars.domain.Planet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static io.wdefassio.starwars.common.PlanetConstants.PLANET;
import static io.wdefassio.starwars.common.PlanetConstants.TATTOINE;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/remove-planets.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/create-planets.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PlanetIT {
    @Autowired
    TestRestTemplate template;

    @Test
    @DisplayName("should be able to create a planet")
    public void createPlanetE2ESuccess() {
        ResponseEntity<Planet> sut = template.postForEntity("/planets", PLANET, Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody().getId()).isNotNull();
        assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());

    }

    @Test
    @DisplayName("should return a planet")
    public void getPlanetE2ESuccess() {
        ResponseEntity<Planet> sut = template.getForEntity("/planets/1", Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody().getId()).isEqualTo(1L);

        assertThat(sut.getBody().getName()).isEqualTo(TATTOINE.getName());
        assertThat(sut.getBody().getTerrain()).isEqualTo(TATTOINE.getTerrain());
        assertThat(sut.getBody().getClimate()).isEqualTo(TATTOINE.getClimate());


    }

    @Test
    @DisplayName("should return a planet")
    public void getPlanetByNameE2ESuccess() {
        ResponseEntity<Planet> sut = template.getForEntity("/planets/name/" + TATTOINE.getName(), Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATTOINE);

        assertThat(sut.getBody().getName()).isEqualTo(TATTOINE.getName());

    }

    @Test
    @DisplayName("should return all planets")
    public void getAllPlanetsE2ESuccess() {
        ResponseEntity<Planet[]> sut = template.getForEntity("/planets", Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(3);
        assertThat(sut.getBody()[0]).isEqualTo(TATTOINE);

    }

    @Test
    @DisplayName("should return planet by climate")
    public void getPlanetByClimateE2ESuccess() {
        ResponseEntity<Planet[]> sut = template.getForEntity("/planets?climate=" + TATTOINE.getClimate(), Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(TATTOINE);

    }

    @Test
    @DisplayName("should return planet by terrain")
    public void getPlanetByTerrainE2ESuccess() {
        ResponseEntity<Planet[]> sut = template.getForEntity("/planets?terrain=" + TATTOINE.getTerrain(), Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(TATTOINE);

    }

    @Test
    @DisplayName("should return no content when delete")
    public void deletePlanetByIdE2ESuccess() {
        ResponseEntity<Void> sut = template.exchange("/planets/" + TATTOINE.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}
