package fr.li212.codingame.tron.infrastructure.path;

import fr.li212.codingame.tron.domain.grid.port.Coordinate;

import java.util.List;

public class Path {
    private final List<Coordinate> path;
    private final boolean exists;

    public Path(final List<Coordinate> path, final boolean exists) {
        this.path = path;
        this.exists = exists;
    }

    public List<Coordinate> getPath() {
        return path;
    }

    public boolean exists() {
        return exists;
    }
}
