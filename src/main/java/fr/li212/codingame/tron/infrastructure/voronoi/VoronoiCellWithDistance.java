package fr.li212.codingame.tron.infrastructure.voronoi;

public class VoronoiCellWithDistance implements Comparable<VoronoiCellWithDistance> {
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

    public VoronoiGerm getGerm() {
        return germ;
    }

    @Override
    public int compareTo(final VoronoiCellWithDistance toCompare) {
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
