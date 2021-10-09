package fr.li212.codingame.tron;

import fr.li212.codingame.tron.adapters.grid.AugmentedBasicSquareGridProvider;
import fr.li212.codingame.tron.adapters.grid.BasicSquareGrid;
import fr.li212.codingame.tron.adapters.grid.SquareCell;
import fr.li212.codingame.tron.adapters.grid.SquareCoordinate;
import fr.li212.codingame.tron.adapters.io.OutputCodingameTurn;
import fr.li212.codingame.tron.domain.parameters.GlobalParameters;
import fr.li212.codingame.tron.domain.PlayTurn;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;
import fr.li212.codingame.tron.domain.port.OutputTurn;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagramProvider;

import java.util.ArrayList;
import java.util.List;

class PlayTurnWithStartConditions {

    private final static VoronoiDiagramProvider VORONOI_DIAGRAM_PROVIDER = new VoronoiDiagramProvider();
    private final static AugmentedBasicSquareGridProvider AUGMENTED_BASIC_SQUARE_GRID_PROVIDER = new AugmentedBasicSquareGridProvider(
            VORONOI_DIAGRAM_PROVIDER);
    private final static OutputTurn OUTPUT_TURN = new OutputCodingameTurn();


    private static final String START_GRID =
                    "                              \n" +
                    "                              \n" +
                    "                              \n" +
                    "         0XXXX                \n" +
                    "          XXXX                \n" +
                    "                              \n" +
                    "         2XXXXX               \n" +
                    "              X               \n" +
                    "              X         XXXXXX\n" +
                    "              X 1XXXXXXXX     \n" +
                    "                       XX     \n" +
                    "                        3     \n" +
                    "                              \n" +
                    "                              \n" +
                    "                              \n" +
                    "                              \n" +
                    "                              \n" +
                    "                              \n" +
                    "                              \n" +
                    "                              \n";

    public static void main(String[] args) throws InterruptedException {

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
                currentGrid,
                playerContexts,
                OUTPUT_TURN);
        playTurn.playMove();
        final long endTime = System.currentTimeMillis() - startTime;
        System.out.println(endTime);
    }

}
