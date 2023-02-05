package io.wdefassio.starwars.application.service;

import io.wdefassio.starwars.domain.Planet;
import io.wdefassio.starwars.infra.repository.PlanetRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.wdefassio.starwars.common.PlanetConstants.INVALID_PLANET;
import static io.wdefassio.starwars.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {
    @InjectMocks
    PlanetServiceImp planetService;
    @Mock
    PlanetRepository planetRepository;

    @Test
    @DisplayName("should be able to create a planet with correct data")
    public void createPlanetSuccess() {
        when(planetRepository.save(PLANET)).thenReturn(PLANET);
        Planet planet = planetService.create(PLANET);
        assertThat(planet).isEqualTo(PLANET);
    }

    @Test
    @DisplayName("should not be able to create a planet with invalid data")
    public void createPlanetFail(){
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

     assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should be able to find a planet by id")
    public void getPlanetSuccess() {
        when(planetRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(PLANET));
        Optional<Planet> planet = planetService.get(1L);
        assertThat(planet).isNotEmpty();
        assertThat(planet.get()).isEqualTo(PLANET);
    }

    @Test
    @DisplayName("should be able to return a empty optional by id")
    public void getPlanetFail() {
        when(planetRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Planet> planet = planetService.get(1L);
        assertThat(planet).isEmpty();
    }

    @Test
    @DisplayName("should be able to find a planet by name")
    public void getByNamePlanetSuccess() {
        when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));
        Optional<Planet> planet = planetService.getByName(PLANET.getName());
        assertThat(planet).isNotEmpty();
        assertThat(planet.get()).isEqualTo(PLANET);
    }

    @Test
    @DisplayName("should be able to return a empty optional by name")
    public void getByNamePlanetFail() {
        when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.empty());
        Optional<Planet> planet = planetService.getByName(PLANET.getName());
        assertThat(planet).isEmpty();
    }
    @Test
    @DisplayName("should return all planets")
    public void listAllPlanetSuccess() {
        List<Planet> planets = List.of(PLANET);

        when(planetRepository.findAll(Mockito.any(Example.class))).thenReturn(planets);

        List<Planet> list = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(list).isNotEmpty();
        assertThat(list).hasSize(1);
        assertThat(list.get(0)).isEqualTo(PLANET);
    }
    @Test
    @DisplayName("should return no planets")
    public void listAllPlanetNoPlants() {
        when(planetRepository.findAll(Mockito.any(Example.class))).thenReturn(Collections.EMPTY_LIST);
        List<Planet> list = planetService.list(PLANET.getTerrain(), PLANET.getClimate());
        assertThat(list).isEmpty();
    }
    @Test
    @DisplayName("should be able to delete a planet by id")
    public void deleteByIdPlanet() {
       assertThatCode(() -> planetService.remove(Mockito.anyLong())).doesNotThrowAnyException();
    }
    @Test
    @DisplayName("should throw when a nonexistent id is provided")
    public void deleteByIdPlanetThrows() {
        doThrow(new RuntimeException()).when(planetRepository).deleteById(Mockito.anyLong());

        assertThatThrownBy(() -> planetService.remove(99L)).isInstanceOf(RuntimeException.class);
    }
}
