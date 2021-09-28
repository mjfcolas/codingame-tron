package fr.li212.codingame.tron.infrastructure.voronoi.printer;

import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiCell;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagram;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiGerm;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiGrid;

import java.util.Collections;
import java.util.Set;

public class PrintVoronoiDiagram {

    public static void print(final VoronoiGrid grid, final VoronoiDiagram voronoiDiagram) {
        final int maxWidth = grid.getVoronoiCells().stream()
                .mapToInt(cell -> ((PrintableVoronoiCell) cell).getX())
                .max().orElse(0);
        final int maxHeight = grid.getVoronoiCells().stream()
                .mapToInt(cell -> ((PrintableVoronoiCell) cell).getY())
                .max().orElse(0);
        final String oneLine = String.join("", Collections.nCopies(maxWidth + 1, "X")) + "\n";
        final StringBuilder builder = new StringBuilder(String.join("", Collections.nCopies(maxHeight + 1, oneLine)));

        int currentSpace = 0;

        for (Set<VoronoiCell> voronoiCells : voronoiDiagram.getVoronoiSpaces().values()) {
            currentSpace++;
            for (VoronoiCell cell : voronoiCells) {
                final PrintableVoronoiCell printableCell = (PrintableVoronoiCell) cell;
                final int positionToReplace = getPositionToReplace(printableCell, maxWidth);
                builder.setCharAt(positionToReplace, String.valueOf(currentSpace).charAt(0));
            }
        }

        for(VoronoiGerm germ: voronoiDiagram.getVoronoiSpaces().keySet()){
            builder.setCharAt(getPositionToReplace((PrintableVoronoiCell) germ.getCell(), maxWidth), '.');
        }
        System.err.println(builder);
    }

    private static int getPositionToReplace(final PrintableVoronoiCell printableCell, final int maxWidth) {
        return printableCell.getY() * (maxWidth + 2) + printableCell.getX();
    }
}
