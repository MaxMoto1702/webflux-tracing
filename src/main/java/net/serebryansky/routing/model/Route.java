package net.serebryansky.routing.model;

import lombok.Data;
import net.serebryansky.place.model.Place;

@Data
public class Route {
    Place placeStart;
    Place placeEnd;
}
