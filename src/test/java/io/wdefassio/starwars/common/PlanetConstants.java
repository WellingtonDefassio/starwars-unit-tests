package io.wdefassio.starwars.common;

import io.wdefassio.starwars.domain.Planet;

public class PlanetConstants {
    public static final Planet PLANET = new Planet(null, "name", "climate", "terrains");
    public static final Planet INVALID_PLANET = new Planet(null, " ", " ", " ");


}
