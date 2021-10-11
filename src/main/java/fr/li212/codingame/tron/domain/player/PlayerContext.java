package fr.li212.codingame.tron.domain.player;

import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.domain.move.Move;

import java.util.Collection;
import java.util.stream.Collectors;

public class PlayerContext {
    private final PlayerIdentifier playerIdentifier;
    private final Coordinate startCoordinate;
    private final Coordinate currentCoordinate;
    private final boolean isControlledPlayerContext;
    private final boolean eliminated;

    public PlayerContext(
            final PlayerIdentifier playerIdentifier,
            final Coordinate startCoordinate,
            final Coordinate currentCoordinate,
            final boolean isControlledPlayerContext,
            final boolean eliminated) {
        this.playerIdentifier = playerIdentifier;
        this.startCoordinate = startCoordinate;
        this.currentCoordinate = currentCoordinate;
        this.isControlledPlayerContext = isControlledPlayerContext;
        this.eliminated = eliminated;
    }

    public static Collection<PlayerContext> predictAllPlayerContextsWithMoveForOneContext(final Collection<PlayerContext> initialPlayerContexts, final PlayerIdentifier playerToMove, final Move moveToPredict) {
        return initialPlayerContexts.stream().map(playerContext -> {
            if (playerContext.playerIdentifier.equals(playerToMove)) {
                return new PlayerContext(
                        playerContext.getPlayerIdentifier(),
                        playerContext.getStartCoordinate(),
                        playerContext.currentCoordinate.adjacentCoordinate(moveToPredict),
                        playerContext.isControlledPlayerContext,
                        playerContext.isEliminated()
                );
            }
            return playerContext;
        }).collect(Collectors.toSet());
    }

    public static PlayerContext predictPlayerContext(final PlayerContext playerContext, final Move moveToPredict) {
        return new PlayerContext(
                playerContext.getPlayerIdentifier(),
                playerContext.getStartCoordinate(),
                playerContext.currentCoordinate.adjacentCoordinate(moveToPredict),
                playerContext.isControlledPlayerContext,
                playerContext.isEliminated()
        );
    }

    public Coordinate getStartCoordinate() {
        return startCoordinate;
    }

    public Coordinate getCurrentCoordinate() {
        return currentCoordinate;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public boolean isControlledPlayerContext() {
        return isControlledPlayerContext;
    }

    public PlayerIdentifier getPlayerIdentifier() {
        return playerIdentifier;
    }

    @Override
    public String toString() {
        return "PlayerContext{" +
                "playerIdentifier=" + playerIdentifier +
                ", startCoordinate=" + startCoordinate +
                ", currentCoordinate=" + currentCoordinate +
                ", isControlledPlayerContext=" + isControlledPlayerContext +
                '}';
    }
}
