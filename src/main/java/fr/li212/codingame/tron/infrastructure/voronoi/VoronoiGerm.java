package fr.li212.codingame.tron.infrastructure.voronoi;

import java.util.Objects;

public class VoronoiGerm {
    private final VoronoiCell cell;

    public VoronoiGerm(final VoronoiCell cell) {
        this.cell = cell;
    }

    public VoronoiCell getCell() {
        return cell;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VoronoiGerm that = (VoronoiGerm) o;
        return Objects.equals(cell, that.cell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell);
    }

    @Override
    public String toString() {
        return "VoronoiGerm{" +
                "cell=" + cell +
                '}';
    }
}
