package fr.li212.codingame.tron;

import fr.li212.codingame.tron.adapters.grid.AugmentedBasicSquareGridProvider;
import fr.li212.codingame.tron.adapters.grid.BasicSquareGrid;
import fr.li212.codingame.tron.adapters.grid.BasicSquareGridProvider;
import fr.li212.codingame.tron.adapters.io.InputCodingameTurn;
import fr.li212.codingame.tron.adapters.io.OutputCodingameTurn;
import fr.li212.codingame.tron.domain.PlayTurn;
import fr.li212.codingame.tron.domain.parameters.GlobalParameters;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.port.OutputTurn;
import fr.li212.codingame.tron.domain.providers.GridProvider;

import java.util.Collection;
import java.util.stream.Collectors;

public class Player {

    private final static AugmentedBasicSquareGridProvider AUGMENTED_BASIC_SQUARE_GRID_PROVIDER = new AugmentedBasicSquareGridProvider();
    private final static GridProvider GRID_PROVIDER = new BasicSquareGridProvider();

    private final static OutputTurn OUTPUT_TURN = new OutputCodingameTurn();
    private final static InputCodingameTurn INPUT_TURN = new InputCodingameTurn();

    private static BasicSquareGrid currentGrid = new BasicSquareGrid(GlobalParameters.GRID_WIDTH, GlobalParameters.GRID_HEIGHT);

    public static void main(String[] args) {
        while (true) {
            final long startTime = System.currentTimeMillis();
            final Collection<PlayerContext> playerContexts = INPUT_TURN.get();
            currentGrid = new BasicSquareGrid(currentGrid, playerContexts);

            final PlayTurn playTurn = new PlayTurn(
                    AUGMENTED_BASIC_SQUARE_GRID_PROVIDER,
                    GRID_PROVIDER,
                    currentGrid,
                    playerContexts.stream().filter(playerContext -> !playerContext.isEliminated()).collect(Collectors.toSet()),
                    playerContexts.stream().filter(PlayerContext::isControlledPlayerContext).map(PlayerContext::getPlayerIdentifier).findFirst().orElseThrow(IllegalStateException::new),
                    OUTPUT_TURN);
            playTurn.playMove();
            System.err.println("TOTAL TIME: " + (System.currentTimeMillis() - startTime));
        }
    }
}
