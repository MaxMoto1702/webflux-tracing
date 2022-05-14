package net.serebryansky.nology;

import lombok.Data;
import lombok.experimental.Accessors;
import net.serebryansky.city.City;
import net.serebryansky.place.Place;
import net.serebryansky.routing.Route;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Accessors(chain = true)
public class Trip {
    private City city;
    private List<Place> places = new CopyOnWriteArrayList<>();
    private List<Route> routes = new CopyOnWriteArrayList<>();
}
