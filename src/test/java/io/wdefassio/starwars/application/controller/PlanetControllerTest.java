package io.wdefassio.starwars.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wdefassio.starwars.application.service.PlanetServiceImp;
import io.wdefassio.starwars.domain.Planet;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.wdefassio.starwars.common.PlanetConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PlanetServiceImp planetService;

    @Test
    @DisplayName("should be able to create a planet with correct data")
    public void createPlanetSuccess() throws Exception {

        when(planetService.create(PLANET)).thenReturn(PLANET);
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpect(jsonPath("$").value(PLANET));

    }

    @Test
    @DisplayName("should return error when a invalid data is provided")
    public void createPlanetInvalid() throws Exception {

        Planet emptyPlanet = new Planet(null, null, null, null);
        Planet invalidPlanet = new Planet(null, "", "", "");

        when(planetService.create(PLANET)).thenReturn(PLANET);
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(emptyPlanet)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
        when(planetService.create(PLANET)).thenReturn(PLANET);
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(invalidPlanet)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

    }

    @Test
    @DisplayName("should return error when a existing planet is provided")
    public void createPlanetSameName() throws Exception {

        when(planetService.create(any(Planet.class))).thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
    }

    @Test
    @DisplayName("should return a planet by id")
    public void findPlanetByIdSuccess() throws Exception {
        when(planetService.get(anyLong())).thenReturn(Optional.of(PLANET));

        mockMvc
                .perform(get("/planets/1")
                        .content(objectMapper
                                .writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$").value(PLANET));

    }

    @Test
    @DisplayName("should return 404 if a planet not found by id")
    public void findPlanetByIdFail() throws Exception {
        when(planetService.get(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/planets/1")
                        .content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return a planet by name")
    public void findPlanetByNameSuccess() throws Exception {

        when(planetService.getByName(anyString())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/name/".concat(PLANET.getName()))
                        .content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    @DisplayName("should return 404 if a planet not found by name")
    public void findPlanetByNameFail() throws Exception {

        when(planetService.getByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/planets/name/".concat(PLANET.getName()))
                        .content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return a list of plants and 200")
    public void findListPlanetByFilters() throws Exception {

        when(planetService.getByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/planets/name/".concat(PLANET.getName()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should be able to filter planets")
    public void filterPlanetsSuccess() throws Exception {
        when(planetService.list(null, null)).thenReturn(PLANETS);
        when(planetService.list(TATTOINE.getTerrain(), TATTOINE.getClimate())).thenReturn(List.of(TATTOINE));

        mockMvc.perform(get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(get("/planets?" + String.format("terrain=%s&climate=%s", TATTOINE.getTerrain(), TATTOINE.getClimate())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(TATTOINE));

    }

    @Test
    @DisplayName("should filter no planets")
    public void filterNoPlanetsSuccess() throws Exception {
        when(planetService.list(null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    @DisplayName("should be able to delete a planet")
    public void deletePlanetSuccess() throws Exception {
        mockMvc.perform(delete("/planets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should throw when a invalid id is provided")
    public void deletePlanetFail() throws Exception {

        final Long planetId = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(planetService).remove(anyLong());

        mockMvc.perform(delete("/planets/" + planetId))
                .andExpect(status().isNotFound());

    }


}
