package fr.li212.codingame.tron.domain;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.Grid;
import fr.li212.codingame.tron.domain.move.CellWithMove;
import fr.li212.codingame.tron.domain.move.Move;
import fr.li212.codingame.tron.domain.move.ScoredMove;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;
import fr.li212.codingame.tron.domain.port.OutputTurn;
import fr.li212.codingame.tron.domain.providers.AugmentedGridProvider;
import fr.li212.codingame.tron.domain.providers.GridProvider;

import java.util.*;
import java.util.stream.Collectors;

public class PlayTurn {
    final AugmentedGridProvider augmentedGridProvider;
    final GridProvider gridProvider;

    final Grid grid;
    final Collection<PlayerContext> opponentContexts;
    final PlayerContext controlledPlayerContext;
    final OutputTurn outputTurn;

    public PlayTurn(
            final AugmentedGridProvider augmentedGridProvider,
            final GridProvider gridProvider,
            final Grid grid,
            final Collection<PlayerContext> playerContexts,
            final PlayerIdentifier playerToPlay,
            final OutputTurn outputTurn) {
        this.augmentedGridProvider = augmentedGridProvider;
        this.gridProvider = gridProvider;
        this.grid = grid;
        this.opponentContexts = playerContexts.stream()
                .filter(playerContext -> !playerContext.getPlayerIdentifier().equals(playerToPlay))
                .collect(Collectors.toSet());
        this.controlledPlayerContext = playerContexts.stream()
                .filter(playerContext -> playerContext.getPlayerIdentifier().equals(playerToPlay))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Impossible to find controlled player context"));
        this.outputTurn = outputTurn;
    }


    public void playMove() {
        this.outputTurn.play(Optional.ofNullable(this.getMove(true)).map(ScoredMove::getMove).orElse(null));
    }

    public ScoredMove getMove(final boolean recursion) {
        if (!recursion) {
            return null;
        }
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
            final float playerScore = augmentedGridForMove.voronoiScore(predictedControlledPlayerContext);
            float playerScore2 = 0;




            final Grid gridAfterPlayerMove = gridProvider.get(grid, allContexts);
            final Map<PlayerContext, Move> predictedOpponentMoves = new HashMap<>();
            opponentContexts.forEach(playerContext -> {
                final PlayTurn playNextTurn = new PlayTurn(
                        this.augmentedGridProvider,
                        this.gridProvider,
                        gridAfterPlayerMove,
                        allContexts,
                        playerContext.getPlayerIdentifier(),
                        outputTurn);
                predictedOpponentMoves.put(playerContext, Optional.ofNullable(playNextTurn.getMove(false)).map(ScoredMove::getMove).orElse(null));
            });
            final Collection<PlayerContext> estimatedAllPlayerContexts = new ArrayList<>();
            estimatedAllPlayerContexts.add(predictedControlledPlayerContext);
            estimatedAllPlayerContexts.addAll(opponentContexts.stream().map(opponentContext -> PlayerContext.predictPlayerContext(opponentContext, predictedOpponentMoves.get(opponentContext))).collect(Collectors.toList()));
            Collection<PlayerContext> opponentContexts2 = estimatedAllPlayerContexts.stream()
                    .filter(playerContext -> !playerContext.getPlayerIdentifier().equals(controlledPlayerContext.getPlayerIdentifier()))
                    .collect(Collectors.toSet());
            final Grid gridForNextTurn = gridProvider.get(grid, estimatedAllPlayerContexts);

            final List<Move> possibleMoves2 = Arrays.stream(Move.values())
                    .map(move2 -> new CellWithMove(
                            gridForNextTurn.getCell(move2.computeMove(predictedControlledPlayerContext.getCurrentCoordinate())),
                            move2))
                    .filter(cellWithMove -> cellWithMove.getCell() != null && cellWithMove.getCell().isAccessible())
                    .map(CellWithMove::getMove).collect(Collectors.toList());
            for (Move move2 : possibleMoves2) {
                final PlayerContext predictedControlledPlayerContext2 = PlayerContext.predictPlayerContext(predictedControlledPlayerContext, move2);
                final Collection<PlayerContext> allContexts2 = new ArrayList<>();
                allContexts2.add(predictedControlledPlayerContext2);
                allContexts2.addAll(opponentContexts2);
                final AugmentedGrid augmentedGridForMove2 = augmentedGridProvider.get(gridForNextTurn, allContexts2);
                playerScore2 = augmentedGridForMove2.voronoiScore(predictedControlledPlayerContext2);
            }





            final float opponentsAccessibleCellNumber = opponentContexts.stream()
                    .map(augmentedGridForMove::voronoiScore)
                    .reduce(0f, Float::sum);
            moves.add(new ScoredMove(move,
                    playerScore + playerScore2,
                    opponentsAccessibleCellNumber,
                    augmentedGridForMove.numberOfLibertiesAfter(predictedControlledPlayerContext)));
        }
        return Optional.ofNullable(moves.poll()).orElse(null);
    }
}
