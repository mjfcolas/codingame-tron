package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

public class BasicSquareGridTest {

    private final static int A_WIDTH = 2;
    private final static int A_HEIGHT = 3;

    private final static SquareCoordinate P1_INITIAL_COORDINATE = new SquareCoordinate(0, 1);
    private final static SquareCoordinate P1_FIRST_CURRENT_COORDINATE = new SquareCoordinate(0, 1);
    private final static SquareCoordinate P1_SECOND_CURRENT_COORDINATE = new SquareCoordinate(1, 2);

    private final static SquareCoordinate P2_INITIAL_COORDINATE = new SquareCoordinate(0, 0);
    private final static SquareCoordinate P2_FIRST_CURRENT_COORDINATE = new SquareCoordinate(0, 0);
    private final static SquareCoordinate P2_SECOND_CURRENT_COORDINATE = new SquareCoordinate(1, 1);

    private final static PlayerContext P1_FIRST_PLAYER_CONTEXT = new PlayerContext(
            new PlayerIdentifier(1),
            P1_INITIAL_COORDINATE,
            P1_FIRST_CURRENT_COORDINATE,
            true);
    private final static PlayerContext P1_SECOND_PLAYER_CONTEXT = new PlayerContext(
            new PlayerIdentifier(1),
            P1_INITIAL_COORDINATE,
            P1_SECOND_CURRENT_COORDINATE,
            true);
    private final static PlayerContext P2_FIRST_PLAYER_CONTEXT = new PlayerContext(
            new PlayerIdentifier(2),
            P2_INITIAL_COORDINATE,
            P2_FIRST_CURRENT_COORDINATE,
            false);
    private final static PlayerContext P2_SECOND_PLAYER_CONTEXT = new PlayerContext(
            new PlayerIdentifier(2),
            P2_INITIAL_COORDINATE,
            P2_SECOND_CURRENT_COORDINATE,
            false);

    @Test
    @DisplayName("Given width and height," +
            "when initializing a grid," +
            "then grid is initialized with correct dimensions")
    void initiateGrid() {
        final BasicSquareGrid grid = new BasicSquareGrid(A_WIDTH, A_HEIGHT);
        Assertions.assertEquals(A_WIDTH * A_HEIGHT, grid.getVoronoiCells().size());
    }
}
