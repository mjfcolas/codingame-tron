package fr.li212.codingame.tron.infrastructure.voronoi.printer;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagram;

import java.util.Collections;
import java.util.Set;

public class PrintVoronoiDiagram {

    public static void print(final Grid grid, final VoronoiDiagram voronoiDiagram) {
        final int width = grid.getWidth();
        final int height = grid.getHeight();
        final String oneLine = String.join("", Collections.nCopies(width, "X")) + "\n";
        final StringBuilder builder = new StringBuilder(String.join("", Collections.nCopies(height, oneLine)));

        int currentSpace = 0;

        for (Set<Cell> voronoiCells : voronoiDiagram.getVoronoiSpaces().values()) {
            currentSpace++;
            for (Cell cell : voronoiCells) {
                final int positionToReplace = getPositionToReplace(cell, width);
                builder.setCharAt(positionToReplace, String.valueOf(currentSpace).charAt(0));
            }
        }

        for (Cell germ : voronoiDiagram.getVoronoiSpaces().keySet()) {
            builder.setCharAt(getPositionToReplace(germ, width), '.');
        }
        System.err.println(builder);
    }

    private static int getPositionToReplace(final Cell printableCell, final int maxWidth) {
        return printableCell.getY() * (maxWidth + 1) + printableCell.getX();
    }
}
