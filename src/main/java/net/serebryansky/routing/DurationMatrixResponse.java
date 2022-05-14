package net.serebryansky.routing;

import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class DurationMatrixResponse {
    List<List<Double>> matrix = new CopyOnWriteArrayList<>();
}
