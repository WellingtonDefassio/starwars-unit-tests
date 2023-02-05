package io.wdefassio.starwars.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wdefassio.starwars.application.service.PlanetServiceImp;
import io.wdefassio.starwars.domain.Planet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

import static io.wdefassio.starwars.common.PlanetConstants.PLANET;
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
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET));

    }

    @Test
    @DisplayName("should return error when a invalid data is provided")
    public void createPlanetInvalid() throws Exception {

        Planet emptyPlanet = new Planet(null, null, null, null);
        Planet invalidPlanet = new Planet(null, "", "", "");

        when(planetService.create(PLANET)).thenReturn(PLANET);
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(emptyPlanet)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
        when(planetService.create(PLANET)).thenReturn(PLANET);
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(invalidPlanet)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }
    @Test
    @DisplayName("should return error when a existing planet is provided")
    public void createPlanetSameName() throws Exception {

        when(planetService.create(any(Planet.class))).thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("should return a planet by id")
    public void findPlanetByIdSuccess() throws Exception {
             when(planetService.get(anyLong())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/1")
                        .content(objectMapper.writeValueAsString(PLANET))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));

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


}
