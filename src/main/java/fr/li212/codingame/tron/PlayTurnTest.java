package fr.li212.codingame.tron;

import fr.li212.codingame.tron.adapters.grid.AugmentedBasicSquareGridProvider;
import fr.li212.codingame.tron.adapters.grid.BasicSquareGrid;
import fr.li212.codingame.tron.adapters.grid.SquareCoordinate;
import fr.li212.codingame.tron.adapters.io.OutputCodingameTurn;
import fr.li212.codingame.tron.domain.parameters.GlobalParameters;
import fr.li212.codingame.tron.domain.PlayTurn;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;
import fr.li212.codingame.tron.domain.port.OutputTurn;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagramProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

class PlayTurnTest {

    private final static VoronoiDiagramProvider VORONOI_DIAGRAM_PROVIDER = new VoronoiDiagramProvider();
    private final static AugmentedBasicSquareGridProvider AUGMENTED_BASIC_SQUARE_GRID_PROVIDER = new AugmentedBasicSquareGridProvider(
            VORONOI_DIAGRAM_PROVIDER);
    private final static OutputTurn OUTPUT_TURN = new OutputCodingameTurn();


    private static final BasicSquareGrid initialGrid = new BasicSquareGrid(GlobalParameters.GRID_WIDTH, GlobalParameters.GRID_HEIGHT);

    private final static SquareCoordinate P1_START = new SquareCoordinate(18, 13);
    private final static SquareCoordinate P2_START = new SquareCoordinate(13, 18);
    private final static SquareCoordinate P3_START = new SquareCoordinate(27, 13);


    private final static Collection<PlayerContext> PLAYER_CONTEXTS = new HashSet<>(Arrays.asList(
            new PlayerContext(
                    new PlayerIdentifier(0),
                    P1_START,
                    P1_START,
                    true,
                    false
            ),
            new PlayerContext(
                    new PlayerIdentifier(1),
                    P2_START,
                    P2_START,
                    false,
                    false
            )
//            ),
//            new PlayerContext(
//                    new PlayerIdentifier(2),
//                    P3_START,
//                    P3_START,
//                    false
//            )
    ));

    public static void main(String[] args) throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final BasicSquareGrid currentGrid = new BasicSquareGrid(initialGrid, PLAYER_CONTEXTS);
        final PlayTurn playTurn = new PlayTurn(
                AUGMENTED_BASIC_SQUARE_GRID_PROVIDER,
                currentGrid,
                PLAYER_CONTEXTS,
                OUTPUT_TURN);
        playTurn.playMove();
        final long endTime = System.currentTimeMillis() - startTime;
        System.out.println(endTime);
    }

}
