package fr.li212.codingame.tron.domain;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.domain.move.CellWithMove;
import fr.li212.codingame.tron.domain.move.Move;
import fr.li212.codingame.tron.domain.move.ScoredMove;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.port.OutputTurn;
import fr.li212.codingame.tron.domain.providers.AugmentedGridProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PlayTurn {
    final AugmentedGridProvider augmentedGridProvider;

    final Grid grid;
    final Collection<PlayerContext> playerContexts;
    final PlayerContext controlledPlayerContext;
    final OutputTurn outputTurn;

    public PlayTurn(
            final AugmentedGridProvider augmentedGridProvider,
            final Grid grid,
            final Collection<PlayerContext> playerContexts,
            final OutputTurn outputTurn) {
        this.augmentedGridProvider = augmentedGridProvider;
        this.grid = grid;
        this.playerContexts = playerContexts;
        this.controlledPlayerContext = playerContexts.stream()
                .filter(PlayerContext::isControlledPlayerContext)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Impossible to find controlled player context"));
        this.outputTurn = outputTurn;
    }

    private Move getMove() {
        final List<ScoredMove> moves = Arrays.stream(Move.values())
                .map(move -> new CellWithMove(
                        grid.getCell(move.computeMove(controlledPlayerContext.getCurrentCoordinate())),
                        move))
                .filter(cellWithMove -> cellWithMove.getCell() != null)
                .map(CellWithMove::getMove)
                .map(move -> {
                    final Collection<PlayerContext> predictedNextPlayerContexts = PlayerContext.predictAllPlayerContextsWithControlledPlayerMove(playerContexts, move);
                    final PlayerContext predictedControlledPlayerContext = PlayerContext.predictControlledPlayerContext(controlledPlayerContext, move);
                    final AugmentedGrid augmentedGridForMove = augmentedGridProvider.get(grid, predictedNextPlayerContexts);
                    return new ScoredMove(move, augmentedGridForMove.voronoiScore(predictedControlledPlayerContext));
                })
                .sorted()
                .collect(Collectors.toList());
        moves.forEach(System.err::println);
        return moves.get(0).getMove();
    }

    public void playMove() {
        this.outputTurn.play(this.getMove());
    }
}
