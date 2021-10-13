package fr.li212.codingame.tron;

import fr.li212.codingame.tron.adapters.grid.*;
import fr.li212.codingame.tron.adapters.io.OutputCodingameTurn;
import fr.li212.codingame.tron.domain.PlayTurn;
import fr.li212.codingame.tron.domain.parameters.GlobalParameters;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;
import fr.li212.codingame.tron.domain.port.OutputTurn;
import fr.li212.codingame.tron.domain.providers.GridProvider;

import java.util.ArrayList;
import java.util.List;

class PlayTurnWithStartConditions {

    private final static AugmentedBasicSquareGridProvider AUGMENTED_BASIC_SQUARE_GRID_PROVIDER = new AugmentedBasicSquareGridProvider();
    private final static GridProvider GRID_PROVIDER = new BasicSquareGridProvider();
    private final static OutputTurn OUTPUT_TURN = new OutputCodingameTurn();


    private static final String START_GRID =
            "                              \n" +
                    "                              \n" +
                    "                              \n" +
                    "                              \n" +
                    "                              \n" +
                    "    X                         \n" +
                    "    XX                        \n" +
                    "     X                        \n" +
                    "     XXXXXX  3                \n" +
                    "          XX X                \n" +
                    "           XXX      XXXXXXXXXX\n" +
                    "                1XXXX         \n" +
                    "  X                           \n" +
                    "  XXX                         \n" +
                    "    XXXXXXXXXXXX              \n" +
                    "    X          X              \n" +
                    "    X          X              \n" +
                    "    XXXXXXXXX0 2              \n" +
                    "                              \n" +
                    "                              ";

    public static void main(String[] args) {

        final SquareCell[][] initialCells = new SquareCell[GlobalParameters.GRID_WIDTH][GlobalParameters.GRID_HEIGHT];
        final List<PlayerContext> playerContexts = new ArrayList<>();
        final String[] lines = START_GRID.split("\n");
        for (int y = 0; y < GlobalParameters.GRID_HEIGHT; y++) {
            for (int x = 0; x < GlobalParameters.GRID_WIDTH; x++) {
                final Character charOnLine = lines[y].charAt(x);
                final SquareCell currentCell = new SquareCell(new SquareCoordinate(x, y), charOnLine.equals(' ') ? null : new PlayerIdentifier(0));
                initialCells[x][y] = currentCell;
                if (charOnLine != ' ' && charOnLine != 'X') {
                    playerContexts.add(new PlayerContext(
                            new PlayerIdentifier(Integer.parseInt(charOnLine.toString())),
                            new SquareCoordinate(x, y),
                            new SquareCoordinate(x, y),
                            charOnLine == '0',
                            false
                    ));
                }
            }
        }

        final long startTime = System.currentTimeMillis();
        final BasicSquareGrid currentGrid = new BasicSquareGrid(GlobalParameters.GRID_WIDTH, GlobalParameters.GRID_HEIGHT, initialCells);
        final PlayTurn playTurn = new PlayTurn(
                AUGMENTED_BASIC_SQUARE_GRID_PROVIDER,
                GRID_PROVIDER,
                currentGrid,
                playerContexts,
                playerContexts.stream().filter(PlayerContext::isControlledPlayerContext).map(PlayerContext::getPlayerIdentifier).findFirst().orElseThrow(IllegalStateException::new),
                OUTPUT_TURN);
        playTurn.playMove();
        final long endTime = System.currentTimeMillis() - startTime;
        System.out.println(endTime);
    }

}
