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


    public void playMove(){
        final List<Move> possibleMoves = Arrays.stream(Move.values())
                .map(move -> new CellWithMove(
                        grid.getCell(move.computeMove(controlledPlayerContext.getCurrentCoordinate())),
                        move))
                .filter(cellWithMove -> cellWithMove.getCell() != null && cellWithMove.getCell().isAccessible())
                .map(CellWithMove::getMove).collect(Collectors.toList());

            final Queue<ScoredMove> moves = new PriorityQueue<>(4);
            for (Move move : possibleMoves) {
                final Collection<PlayerContext> predictedNextPlayerContexts = PlayerContext.predictAllPlayerContextsWithControlledPlayerMove(playerContexts, move);
                final PlayerContext predictedControlledPlayerContext = PlayerContext.predictControlledPlayerContext(controlledPlayerContext, move);
                final AugmentedGrid augmentedGridForMove = augmentedGridProvider.get(grid, predictedNextPlayerContexts);
                moves.add(new ScoredMove(move, augmentedGridForMove.voronoiScore(predictedControlledPlayerContext)));
            }
            moves.forEach(System.err::println);
            this.outputTurn.play(moves.remove().getMove());
    }

}
