package io.wdefassio.starwars.common;

import io.wdefassio.starwars.domain.Planet;

import java.util.ArrayList;
import java.util.List;

public class PlanetConstants {
    public static final Planet PLANET = new Planet(null, "name", "climate", "terrains");
    public static final Planet INVALID_PLANET = new Planet(null, " ", " ", " ");
    public static final Planet TATTOINE = new Planet(1L, "Tatooine", "arid", "desert");
    public static final Planet ALDERAAN = new Planet(2L, "Alderaan", "temperate", "grass");
    public static final Planet YAVINI = new Planet(3L, "Yavin IV", "temperate, tropical", "grass");
    public static final List<Planet> PLANETS = new ArrayList<>() {
        {
            add(TATTOINE);
            add(ALDERAAN);
            add(YAVINI);
        }
    };


}
