package fr.li212.codingame.tron.domain;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.domain.move.CellWithMove;
import fr.li212.codingame.tron.domain.move.Move;
import fr.li212.codingame.tron.domain.move.ScoredMove;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.port.OutputTurn;
import fr.li212.codingame.tron.domain.providers.AugmentedGridProvider;

import java.util.*;
import java.util.stream.Collectors;

public class PlayTurn {
    final AugmentedGridProvider augmentedGridProvider;

    final Grid grid;
    final Collection<PlayerContext> opponentContexts;
    final PlayerContext controlledPlayerContext;
    final OutputTurn outputTurn;

    public PlayTurn(
            final AugmentedGridProvider augmentedGridProvider,
            final Grid grid,
            final Collection<PlayerContext> playerContexts,
            final OutputTurn outputTurn) {
        this.augmentedGridProvider = augmentedGridProvider;
        this.grid = grid;
        this.opponentContexts = playerContexts.stream()
                .filter(playerContext -> !playerContext.isControlledPlayerContext())
                .collect(Collectors.toSet());
        this.controlledPlayerContext = playerContexts.stream()
                .filter(PlayerContext::isControlledPlayerContext)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Impossible to find controlled player context"));
        this.outputTurn = outputTurn;
    }


    public void playMove() {
        final List<Move> possibleMoves = Arrays.stream(Move.values())
                .map(move -> new CellWithMove(
                        grid.getCell(move.computeMove(controlledPlayerContext.getCurrentCoordinate())),
                        move))
                .filter(cellWithMove -> cellWithMove.getCell() != null && cellWithMove.getCell().isAccessible())
                .map(CellWithMove::getMove).collect(Collectors.toList());

        Queue<ScoredMove> moves = new PriorityQueue<>(4);
        for (Move move : possibleMoves) {
            final PlayerContext predictedControlledPlayerContext = PlayerContext.predictPlayerContext(controlledPlayerContext, move);
            final Collection<PlayerContext> allContexts = new ArrayList<>();
            allContexts.add(predictedControlledPlayerContext);
            allContexts.addAll(opponentContexts);
            final AugmentedGrid augmentedGridForMove = augmentedGridProvider.get(grid, allContexts);
            final float opponentsAccessibleCellNumber = opponentContexts.stream()
                    .map(augmentedGridForMove::voronoiScore)
                    .reduce(0f, Float::sum);
            moves.add(new ScoredMove(move,
                    augmentedGridForMove.voronoiScore(predictedControlledPlayerContext),
                    opponentsAccessibleCellNumber,
                    augmentedGridForMove.numberOfLibertiesAfter(predictedControlledPlayerContext)));
        }
        this.outputTurn.play(moves.remove().getMove());
    }
}
