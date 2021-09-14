package fr.li212.codingame.tron.infrastructure.voronoi;

public class VoronoiCellWithDistance {
    private final VoronoiGerm germ;
    private final VoronoiCell cell;
    private final int distance;

    public VoronoiCellWithDistance(
            final VoronoiGerm germ,
            final VoronoiCell cell,
            final int distance) {
        this.germ = germ;
        this.cell = cell;
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }

    public VoronoiCell getCell() {
        return cell;
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
