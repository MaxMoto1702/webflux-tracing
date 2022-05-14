package net.serebryansky.builder.model;

import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class TripBuilderResponse {
    List<String> placeIds = new CopyOnWriteArrayList<>();
}
