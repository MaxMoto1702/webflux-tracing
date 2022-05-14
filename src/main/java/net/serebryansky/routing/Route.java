package net.serebryansky.routing;

import lombok.Data;
import net.serebryansky.place.Place;

@Data
public class Route {
    Place placeStart;
    Place placeEnd;
}
