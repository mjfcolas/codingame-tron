package fr.li212.codingame.tron.domain;

import fr.li212.codingame.tron.adapters.grid.AugmentedBasicSquareGridProvider;
import fr.li212.codingame.tron.adapters.grid.BasicSquareGrid;
import fr.li212.codingame.tron.adapters.grid.SquareCoordinate;
import fr.li212.codingame.tron.adapters.io.OutputCodingameTurn;
import fr.li212.codingame.tron.adapters.parameters.GlobalParameters;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;
import fr.li212.codingame.tron.domain.port.OutputTurn;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagramProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

class PlayTurnTest {

    private final static VoronoiDiagramProvider VORONOI_DIAGRAM_PROVIDER = new VoronoiDiagramProvider();
    private final static AugmentedBasicSquareGridProvider AUGMENTED_BASIC_SQUARE_GRID_PROVIDER = new AugmentedBasicSquareGridProvider(
            VORONOI_DIAGRAM_PROVIDER);
    private final static OutputTurn OUTPUT_TURN = new OutputCodingameTurn();


    private static final BasicSquareGrid initialGrid = new BasicSquareGrid(GlobalParameters.GRID_WIDTH, GlobalParameters.GRID_HEIGHT);

    private final static SquareCoordinate P1_START = new SquareCoordinate(0, 0);
    private final static SquareCoordinate P2_START = new SquareCoordinate(2, 0);


    private final static Collection<PlayerContext> PLAYER_CONTEXTS = new HashSet<>(Arrays.asList(
            new PlayerContext(
                    new PlayerIdentifier(0),
                    P1_START,
                    P1_START,
                    true
            ),
            new PlayerContext(
                    new PlayerIdentifier(1),
                    P2_START,
                    P2_START,
                    false
            )
    ));

    @Nested
    @DisplayName("Play move")
    class PlayMove {

        @DisplayName("Given a codingame grid and 2 players," +
                "when playing first move," +
                "then total execution time must be below 100ms")
        @Test
        void playFirstMove() {
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
            Assertions.assertTrue(endTime < 110);
        }
    }

}
