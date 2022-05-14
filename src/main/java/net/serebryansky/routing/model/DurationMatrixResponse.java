package net.serebryansky.routing.model;

import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class DurationMatrixResponse {
    List<List<Double>> matrix = new CopyOnWriteArrayList<>();
}
