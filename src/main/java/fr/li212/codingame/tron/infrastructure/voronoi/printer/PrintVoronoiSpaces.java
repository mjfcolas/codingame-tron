package fr.li212.codingame.tron.infrastructure.voronoi.printer;

import fr.li212.codingame.tron.domain.grid.Coordinate;
import fr.li212.codingame.tron.domain.grid.Grid;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class PrintVoronoiSpaces {

    public static void print(final Grid grid, final Map<Coordinate, Set<Coordinate>> voronoiSpaces) {
        final int width = grid.getWidth();
        final int height = grid.getHeight();
        final String oneLine = String.join("", Collections.nCopies(width, "X")) + "\n";
        final StringBuilder builder = new StringBuilder(String.join("", Collections.nCopies(height, oneLine)));

        int currentSpace = 0;

        for (Set<Coordinate> coordinatesInSpaces : voronoiSpaces.values()) {
            currentSpace++;
            for (Coordinate currentCoordinates : coordinatesInSpaces) {
                final int positionToReplace = getPositionToReplace(currentCoordinates, width);
                builder.setCharAt(positionToReplace, String.valueOf(currentSpace).charAt(0));
            }
        }

        for (Coordinate germ : voronoiSpaces.keySet()) {
            builder.setCharAt(getPositionToReplace(germ, width), '.');
        }
        System.err.println(builder);
    }

    private static int getPositionToReplace(final Coordinate coordinate, final int maxWidth) {
        return coordinate.getY() * (maxWidth + 1) + coordinate.getX();
    }
}
