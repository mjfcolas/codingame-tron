package fr.li212.codingame.tron.domain;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.Grid;
import fr.li212.codingame.tron.domain.move.Move;
import fr.li212.codingame.tron.domain.move.ScoredMove;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;
import fr.li212.codingame.tron.domain.providers.AugmentedGridProvider;
import fr.li212.codingame.tron.domain.providers.GridProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class PredictNextGridWithScore {
    final AugmentedGridProvider augmentedGridProvider;
    final GridProvider gridProvider;

    private final Grid initialGrid;
    private final PlayerContext currentPlayer;
    final Collection<PlayerContext> otherPlayers;

    public PredictNextGridWithScore(
            final AugmentedGridProvider augmentedGridProvider,
            final GridProvider gridProvider,
            final Grid initialGrid,
            final Collection<PlayerContext> playerContexts,
            final PlayerIdentifier playerToPlay) {
        this.augmentedGridProvider = augmentedGridProvider;
        this.gridProvider = gridProvider;
        this.initialGrid = initialGrid;
        this.currentPlayer = playerContexts.stream()
                .filter(playerContext -> playerContext.getPlayerIdentifier().equals(playerToPlay))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Impossible to find controlled player context"));
        this.otherPlayers = playerContexts.stream()
                .filter(playerContext -> !playerContext.getPlayerIdentifier().equals(playerToPlay))
                .collect(Collectors.toSet());
    }

    public GridWithScore predict(final Move move) {
        final PlayerContext predictedCurrentPlayerContext = PlayerContext.predictPlayerContext(currentPlayer, move);
        final Collection<PlayerContext> allContexts = new ArrayList<>();
        allContexts.add(predictedCurrentPlayerContext);
        allContexts.addAll(otherPlayers);
        final AugmentedGrid augmentedGridForMove = augmentedGridProvider.get(initialGrid, allContexts);

        final float opponentsAccessibleCellNumber = otherPlayers.stream()
                .map(augmentedGridForMove::voronoiScore)
                .reduce(0f, Float::sum);

        final ScoredMove score = new ScoredMove(move,
                augmentedGridForMove.voronoiScore(predictedCurrentPlayerContext),
                opponentsAccessibleCellNumber,
                augmentedGridForMove.numberOfLibertiesAfter(predictedCurrentPlayerContext));

        final Collection<PlayerContext> predictedContexts = new ArrayList<>();
        predictedContexts.add(PlayerContext.predictPlayerContext(currentPlayer, move));
        predictedContexts.addAll(otherPlayers);
        return new GridWithScore(
                gridProvider.get(initialGrid, predictedContexts),
                currentPlayer,
                move,
                score.getScore());

    }
}
