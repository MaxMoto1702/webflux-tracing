package net.serebryansky.nology.model;

import lombok.Data;
import lombok.experimental.Accessors;
import net.serebryansky.city.model.City;
import net.serebryansky.place.model.Place;
import net.serebryansky.routing.model.Route;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Accessors(chain = true)
public class Trip {
    private City city;
    private List<Place> places = new CopyOnWriteArrayList<>();
    private List<Route> routes = new CopyOnWriteArrayList<>();
}
