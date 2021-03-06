package fr.li212.codingame.tron.domain;

import fr.li212.codingame.tron.adapters.grid.BasicSquareGrid;
import fr.li212.codingame.tron.adapters.grid.printer.BasicSquareGridPrinter;
import fr.li212.codingame.tron.domain.grid.Coordinate;
import fr.li212.codingame.tron.domain.grid.Grid;
import fr.li212.codingame.tron.domain.move.CellWithMove;
import fr.li212.codingame.tron.domain.move.Move;
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
    final Collection<PlayerContext> allPlayerContexts;
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
        this.allPlayerContexts = playerContexts;
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
        this.outputTurn.play(this.getMove());
    }

    public Move getMove() {
        BasicSquareGridPrinter.print((BasicSquareGrid) grid, allPlayerContexts);
        final List<Move> possibleMoves = getPossibleMoves(controlledPlayerContext.getCurrentCoordinate(), grid);

        Move bestMove = null;
        Integer bestScore = null;
        for (Move move : possibleMoves) {
            final int scoreWithDepth = this.getScoreWithDepth(move, grid, allPlayerContexts, 2, 1, 0);

            if(bestScore == null || scoreWithDepth > bestScore ){
                bestMove = move;
                bestScore = scoreWithDepth;
            }
        }
        return bestMove;

    }

    public int getScoreWithDepth(
            final Move move,
            final Grid initialGrid,
            Collection<PlayerContext> playerContextsForDepth,
            final int maxDepth,
            final int currentDepth,
            final int initialScore) {

        final PredictNextGridWithScore predictNextGridWithScore = new PredictNextGridWithScore(
                augmentedGridProvider,
                gridProvider,
                initialGrid,
                playerContextsForDepth,
                controlledPlayerContext.getPlayerIdentifier()
        );
        final GridWithScore currentDepthGridAfterPlayerMove;
        if(currentDepth == 1){
            currentDepthGridAfterPlayerMove = predictNextGridWithScore.predict(move);
        }else {
            final PlayerContext controlledPlayerContext = playerContextsForDepth.stream().filter(PlayerContext::isControlledPlayerContext).findFirst().orElseThrow(IllegalStateException::new);
            currentDepthGridAfterPlayerMove = simulateGivenPlayerTurn(initialGrid, playerContextsForDepth, controlledPlayerContext);
        }
        final int updatedScore = initialScore
                + (currentDepthGridAfterPlayerMove != null ? currentDepthGridAfterPlayerMove.getScore() : 0);
        if (currentDepth == maxDepth) {
            return initialScore + updatedScore;
        }

        final Collection<PlayerContext> nextPlayerContexts = new ArrayList<>(4);
        nextPlayerContexts.add(PlayerContext.predictPlayerContext(currentDepthGridAfterPlayerMove.getSourcePlayer(), currentDepthGridAfterPlayerMove.getSourceMove()));

        GridWithScore initialNextDepthGrid = currentDepthGridAfterPlayerMove;
        for (PlayerContext opponentContext : opponentContexts) {
            GridWithScore potentialNextDepthGrid = simulateGivenPlayerTurn(currentDepthGridAfterPlayerMove.getGrid(), playerContextsForDepth, opponentContext);
            if(potentialNextDepthGrid != null){
                initialNextDepthGrid = potentialNextDepthGrid;
            }
            nextPlayerContexts.add(PlayerContext.predictPlayerContext(initialNextDepthGrid.getSourcePlayer(), initialNextDepthGrid.getSourceMove()));
        }

        return getScoreWithDepth(currentDepthGridAfterPlayerMove.getSourceMove(), initialNextDepthGrid.getGrid(), nextPlayerContexts, maxDepth, currentDepth + 1, updatedScore);
    }

    private GridWithScore simulateGivenPlayerTurn(final Grid initialGrid, Collection<PlayerContext> playerContextsForDepth, final PlayerContext player) {
        final PredictNextGridWithScore predictNextGridWithScore = new PredictNextGridWithScore(
                augmentedGridProvider,
                gridProvider,
                initialGrid,
                playerContextsForDepth,
                player.getPlayerIdentifier()
        );
        GridWithScore potentialGridAfterOpponentMove = null;
        final List<Move> possibleMoves = getPossibleMoves(player.getCurrentCoordinate(), initialGrid);

        for (Move move : possibleMoves) {
            final GridWithScore currentPlayerGrid = predictNextGridWithScore.predict(move);
            if (potentialGridAfterOpponentMove == null
                    || currentPlayerGrid.getScore() > potentialGridAfterOpponentMove.getScore()) {
                potentialGridAfterOpponentMove = currentPlayerGrid;
            }
        }
        return potentialGridAfterOpponentMove;
    }

    private List<Move> getPossibleMoves(final Coordinate baseCoordinates, final Grid gridToCheck){
        return Arrays.stream(Move.values())
                .map(move -> new CellWithMove(
                        gridToCheck.getCell(move.computeMove(baseCoordinates)),
                        move))
                .filter(cellWithMove -> cellWithMove.getCell() != null && cellWithMove.getCell().isAccessible())
                .map(CellWithMove::getMove).collect(Collectors.toList());
    }

//    public ScoredMove getMove(final boolean recursion) {
//        if (!recursion) {
//            return null;
//        }
//        final List<Move> possibleMoves = Arrays.stream(Move.values())
//                .map(move -> new CellWithMove(
//                        grid.getCell(move.computeMove(controlledPlayerContext.getCurrentCoordinate())),
//                        move))
//                .filter(cellWithMove -> cellWithMove.getCell() != null && cellWithMove.getCell().isAccessible())
//                .map(CellWithMove::getMove).collect(Collectors.toList());
//
//        Queue<ScoredMove> moves = new PriorityQueue<>(4);
//        for (Move move : possibleMoves) {
//            final PlayerContext predictedControlledPlayerContext = PlayerContext.predictPlayerContext(controlledPlayerContext, move);
//            final Collection<PlayerContext> allContexts = new ArrayList<>();
//            allContexts.add(predictedControlledPlayerContext);
//            allContexts.addAll(opponentContexts);
//            final AugmentedGrid augmentedGridForMove = augmentedGridProvider.get(grid, allContexts);
//            final float playerScore = augmentedGridForMove.voronoiScore(predictedControlledPlayerContext);
//            float playerScore2 = 0;
//
//
//            final Grid gridAfterPlayerMove = gridProvider.get(grid, allContexts);
//            final Map<PlayerContext, Move> predictedOpponentMoves = new HashMap<>();
//            opponentContexts.forEach(playerContext -> {
//                final PlayTurn playNextTurn = new PlayTurn(
//                        this.augmentedGridProvider,
//                        this.gridProvider,
//                        gridAfterPlayerMove,
//                        allContexts,
//                        playerContext.getPlayerIdentifier(),
//                        outputTurn);
//                predictedOpponentMoves.put(playerContext, Optional.ofNullable(playNextTurn.getMove(false)).map(ScoredMove::getMove).orElse(null));
//            });
//            final Collection<PlayerContext> estimatedAllPlayerContexts = new ArrayList<>();
//            estimatedAllPlayerContexts.add(predictedControlledPlayerContext);
//            estimatedAllPlayerContexts.addAll(opponentContexts.stream().map(opponentContext -> PlayerContext.predictPlayerContext(opponentContext, predictedOpponentMoves.get(opponentContext))).collect(Collectors.toList()));
//            Collection<PlayerContext> opponentContexts2 = estimatedAllPlayerContexts.stream()
//                    .filter(playerContext -> !playerContext.getPlayerIdentifier().equals(controlledPlayerContext.getPlayerIdentifier()))
//                    .collect(Collectors.toSet());
//            final Grid gridForNextTurn = gridProvider.get(grid, estimatedAllPlayerContexts);
//
//            final List<Move> possibleMoves2 = Arrays.stream(Move.values())
//                    .map(move2 -> new CellWithMove(
//                            gridForNextTurn.getCell(move2.computeMove(predictedControlledPlayerContext.getCurrentCoordinate())),
//                            move2))
//                    .filter(cellWithMove -> cellWithMove.getCell() != null && cellWithMove.getCell().isAccessible())
//                    .map(CellWithMove::getMove).collect(Collectors.toList());
//            for (Move move2 : possibleMoves2) {
//                final PlayerContext predictedControlledPlayerContext2 = PlayerContext.predictPlayerContext(predictedControlledPlayerContext, move2);
//                final Collection<PlayerContext> allContexts2 = new ArrayList<>();
//                allContexts2.add(predictedControlledPlayerContext2);
//                allContexts2.addAll(opponentContexts2);
//                final AugmentedGrid augmentedGridForMove2 = augmentedGridProvider.get(gridForNextTurn, allContexts2);
//                playerScore2 = augmentedGridForMove2.voronoiScore(predictedControlledPlayerContext2);
//            }
//
//
//            final float opponentsAccessibleCellNumber = opponentContexts.stream()
//                    .map(augmentedGridForMove::voronoiScore)
//                    .reduce(0f, Float::sum);
//            moves.add(new ScoredMove(move,
//                    playerScore + playerScore2,
//                    opponentsAccessibleCellNumber,
//                    augmentedGridForMove.numberOfLibertiesAfter(predictedControlledPlayerContext)));
//        }
//        return Optional.ofNullable(moves.poll()).orElse(null);
//    }
}
