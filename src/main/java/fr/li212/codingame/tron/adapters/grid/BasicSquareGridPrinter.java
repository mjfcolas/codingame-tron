package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.player.PlayerContext;

import java.util.Collection;
import java.util.Collections;

public class BasicSquareGridPrinter {
    public static void print(final BasicSquareGrid grid,
                             final Collection<PlayerContext> playerContexts) {
        final String oneLine = String.join("", Collections.nCopies(grid.getWidth(), "X")) + "\n";
        final StringBuilder builder = new StringBuilder(String.join("", Collections.nCopies(grid.getHeight(), oneLine)));

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                final SquareCell cell = grid.getCells()[x][y];
                final int positionToReplace = getPositionToReplace(cell, grid.getWidth());
                builder.setCharAt(positionToReplace, cell.isAccessible() ? ' ' : 'X');
            }
        }

        for (PlayerContext playerContext : playerContexts) {
            builder.setCharAt(
                    getPositionToReplace(
                            grid.getCells()[playerContext.getCurrentCoordinate().getX()][playerContext.getCurrentCoordinate().getY()],
                            grid.getWidth()),
                    String.valueOf(playerContext.getPlayerIdentifier().getPlayerNumber()).charAt(0));
        }
        System.err.println(builder);
    }

    private static int getPositionToReplace(final SquareCell cell, final int width) {
        return cell.getY() * (width + 1) + cell.getX();
    }
}
