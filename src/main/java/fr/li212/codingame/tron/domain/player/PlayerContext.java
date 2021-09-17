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

    public PlayerContext(
            final PlayerIdentifier playerIdentifier,
            final Coordinate startCoordinate,
            final Coordinate currentCoordinate,
            final boolean isControlledPlayerContext) {
        this.playerIdentifier = playerIdentifier;
        this.startCoordinate = startCoordinate;
        this.currentCoordinate = currentCoordinate;
        this.isControlledPlayerContext = isControlledPlayerContext;
    }

    public static Collection<PlayerContext> predictAllPlayerContextsWithControlledPlayerMove(final Collection<PlayerContext> initialPlayerContexts, final Move moveToPredict){
        return initialPlayerContexts.stream().map(playerContext -> {
            if(playerContext.isControlledPlayerContext()){
                return new PlayerContext(
                        playerContext.getPlayerIdentifier(),
                        playerContext.getStartCoordinate(),
                        playerContext.currentCoordinate.adjacentCoordinate(moveToPredict),
                        playerContext.isControlledPlayerContext
                );
            }
            return playerContext;
        }).collect(Collectors.toSet());
    }

    public static PlayerContext predictControlledPlayerContext(final PlayerContext playerContext, final Move moveToPredict){
        return new PlayerContext(
                playerContext.getPlayerIdentifier(),
                playerContext.getStartCoordinate(),
                playerContext.currentCoordinate.adjacentCoordinate(moveToPredict),
                playerContext.isControlledPlayerContext
        );
    }

    public Coordinate getStartCoordinate() {
        return startCoordinate;
    }

    public Coordinate getCurrentCoordinate() {
        return currentCoordinate;
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