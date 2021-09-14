package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.move.Move;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SquareCoordinateTest {

    private final static SquareCoordinate ONE_ONE_COORDINATE = new SquareCoordinate(1, 1);
    private final static SquareCoordinate ONE_TWO_COORDINATE = new SquareCoordinate(1, 2);
    private final static SquareCoordinate TWO_ONE_COORDINATE = new SquareCoordinate(2, 1);
    private final static SquareCoordinate ZERO_ONE_COORDINATE = new SquareCoordinate(0, 1);
    private final static SquareCoordinate ONE_ZERO_COORDINATE = new SquareCoordinate(1, 0);

    private final static SquareCoordinate THREE_TWO_COORDINATE = new SquareCoordinate(3, 2);


    @DisplayName("Adjacent coordinate")
    @Nested
    class AdjacentCoordinate {

        @DisplayName("Given 1 1 coordinate, " +
                "when asking for adjacent coordinates, " +
                "then return expected coordinates")
        @Test
        void downCoordinate() {
            Assertions.assertEquals(ONE_TWO_COORDINATE, ONE_ONE_COORDINATE.adjacentCoordinate(Move.DOWN));
            Assertions.assertEquals(ZERO_ONE_COORDINATE, ONE_ONE_COORDINATE.adjacentCoordinate(Move.LEFT));
            Assertions.assertEquals(ONE_ZERO_COORDINATE, ONE_ONE_COORDINATE.adjacentCoordinate(Move.UP));
            Assertions.assertEquals(TWO_ONE_COORDINATE, ONE_ONE_COORDINATE.adjacentCoordinate(Move.RIGHT));

        }
    }

    @DisplayName("Distance")
    @Nested
    class Distance {

        @DisplayName("Given 1 1 coordinate and 3 2 coordinate, " +
                "when asking for distance, " +
                "then return expected distance")
        @Test
        void distance() {
            Assertions.assertEquals(3, ONE_ONE_COORDINATE.distance(THREE_TWO_COORDINATE));
            Assertions.assertEquals(3, THREE_TWO_COORDINATE.distance(ONE_ONE_COORDINATE));
        }
    }
}
