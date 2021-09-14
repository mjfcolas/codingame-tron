package fr.li212.codingame.tron.domain.player;

import java.util.Objects;

public class PlayerIdentifier {
    private final int playerNumber;

    public PlayerIdentifier(final int playerNumber) {
        this.playerNumber = playerNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PlayerIdentifier that = (PlayerIdentifier) o;
        return playerNumber == that.playerNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerNumber);
    }

    @Override
    public String toString() {
        return "PlayerIdentifier{" +
                "playerNumber=" + playerNumber +
                '}';
    }
}
