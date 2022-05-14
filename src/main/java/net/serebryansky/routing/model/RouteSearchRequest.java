package net.serebryansky.routing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.serebryansky.place.model.Place;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteSearchRequest {
    Place placeStart;
    Place placeEnd;
}
