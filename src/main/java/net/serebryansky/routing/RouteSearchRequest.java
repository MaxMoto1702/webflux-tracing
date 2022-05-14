package net.serebryansky.routing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.serebryansky.place.Place;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteSearchRequest {
    Place placeStart;
    Place placeEnd;
}
