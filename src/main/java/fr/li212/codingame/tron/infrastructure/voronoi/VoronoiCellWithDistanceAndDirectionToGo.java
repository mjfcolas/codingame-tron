package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.domain.move.Move;

public class VoronoiCellWithDistanceAndDirectionToGo {
    private final VoronoiGerm germ;
    private final VoronoiCell cell;
    private final int distance;
    private final Move firstMoveToGo;

    public VoronoiCellWithDistanceAndDirectionToGo(
            final VoronoiGerm germ,
            final VoronoiCell cell,
            final int distance,
            final Move firstMoveToGo) {
        this.germ = germ;
        this.cell = cell;
        this.distance = distance;
        this.firstMoveToGo = firstMoveToGo;
    }

    public Integer getDistance() {
        return distance;
    }

    public Move getFirstMoveToGo() {
        return firstMoveToGo;
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
