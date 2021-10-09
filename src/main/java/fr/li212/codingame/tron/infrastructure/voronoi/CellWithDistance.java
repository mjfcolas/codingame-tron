package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.domain.grid.port.Cell;

public class CellWithDistance implements Comparable<CellWithDistance> {
    private final Cell germ;
    private final Cell cell;
    private final int distance;

    public CellWithDistance(
            final Cell germ,
            final Cell cell,
            final int distance) {
        this.germ = germ;
        this.cell = cell;
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }

    public Cell getCell() {
        return cell;
    }

    public Cell getGerm() {
        return germ;
    }

    @Override
    public int compareTo(final CellWithDistance toCompare) {
        return Integer.compare(this.distance, toCompare.distance);
    }

    @Override
    public String toString() {
        return "VoronoiCellWithDistance{" +
                "germ=" + germ +
                ", cell=" + cell +
                ", distance=" + distance +
                '}';
    }
}
