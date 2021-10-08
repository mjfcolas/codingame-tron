package fr.li212.codingame.tron.domain;

import fr.li212.codingame.tron.adapters.parameters.GlobalParameters;
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


    public void playMove() throws InterruptedException {
        final List<Move> possibleMoves = Arrays.stream(Move.values())
                .map(move -> new CellWithMove(
                        grid.getCell(move.computeMove(controlledPlayerContext.getCurrentCoordinate())),
                        move))
                .filter(cellWithMove -> cellWithMove.getCell() != null && cellWithMove.getCell().isAccessible())
                .map(CellWithMove::getMove).collect(Collectors.toList());

        try {
            final Queue<ScoredMove> moves = new PriorityQueue<>(4);
            for (Move move : possibleMoves) {
                final Collection<PlayerContext> predictedNextPlayerContexts = PlayerContext.predictAllPlayerContextsWithControlledPlayerMove(playerContexts, move);
                final PlayerContext predictedControlledPlayerContext = PlayerContext.predictControlledPlayerContext(controlledPlayerContext, move);
                final AugmentedGrid augmentedGridForMove = augmentedGridProvider.get(grid, predictedNextPlayerContexts);
                moves.add(new ScoredMove(move, augmentedGridForMove.voronoiScore(predictedControlledPlayerContext)));
            }
            moves.forEach(System.err::println);
            this.outputTurn.play(moves.remove().getMove());
        } catch (InterruptedException e) {
            System.err.println("INTERRUPTION OF COMPUTATION");
            this.outputTurn.play(possibleMoves.get(0));
        }

//        Thread t = new Thread(new GetMoveTask(
//                augmentedGridProvider,
//                outputTurn,
//                grid,
//                playerContexts,
//                controlledPlayerContext
//        ));
//        Timer timer = new Timer();
//        timer.schedule(new TimeOutTask(t, timer), GlobalParameters.AVAILABLE_COMPUTE_TIME);
//        t.start();
//        t.join();
    }

//    static class GetMoveTask implements Runnable {
//        private final AugmentedGridProvider augmentedGridProvider;
//        private final OutputTurn outputTurn;
//
//        private final Grid grid;
//        private final Collection<PlayerContext> playerContexts;
//        private final PlayerContext controlledPlayerContext;
//
//        GetMoveTask(
//                final AugmentedGridProvider augmentedGridProvider,
//                final OutputTurn outputTurn,
//                final Grid grid,
//                final Collection<PlayerContext> playerContexts,
//                final PlayerContext controlledPlayerContext) {
//            this.augmentedGridProvider = augmentedGridProvider;
//            this.outputTurn = outputTurn;
//            this.grid = grid;
//            this.playerContexts = playerContexts;
//            this.controlledPlayerContext = controlledPlayerContext;
//        }
//
//        @Override
//        public void run() {
//            final List<Move> possibleMoves = Arrays.stream(Move.values())
//                    .map(move -> new CellWithMove(
//                            grid.getCell(move.computeMove(controlledPlayerContext.getCurrentCoordinate())),
//                            move))
//                    .filter(cellWithMove -> cellWithMove.getCell() != null && cellWithMove.getCell().isAccessible())
//                    .map(CellWithMove::getMove).collect(Collectors.toList());
//
//            try {
//                final Queue<ScoredMove> moves = new PriorityQueue<>(4);
//                for (Move move : possibleMoves) {
//                    final Collection<PlayerContext> predictedNextPlayerContexts = PlayerContext.predictAllPlayerContextsWithControlledPlayerMove(playerContexts, move);
//                    final PlayerContext predictedControlledPlayerContext = PlayerContext.predictControlledPlayerContext(controlledPlayerContext, move);
//                    final AugmentedGrid augmentedGridForMove = augmentedGridProvider.get(grid, predictedNextPlayerContexts);
//                    moves.add(new ScoredMove(move, augmentedGridForMove.voronoiScore(predictedControlledPlayerContext)));
//                }
//                moves.forEach(System.err::println);
//                this.outputTurn.play(moves.remove().getMove());
//            } catch (InterruptedException e) {
//                System.err.println("INTERRUPTION OF COMPUTATION");
//                this.outputTurn.play(possibleMoves.get(0));
//            }
//        }
//    }
//
//    static class TimeOutTask extends TimerTask {
//        private final Thread t;
//        private final Timer timer;
//
//        TimeOutTask(Thread t, Timer timer) {
//            this.t = t;
//            this.timer = timer;
//        }
//
//        public void run() {
//            if (t != null && t.isAlive()) {
//                t.interrupt();
//                timer.cancel();
//            }
//        }
//    }
}
