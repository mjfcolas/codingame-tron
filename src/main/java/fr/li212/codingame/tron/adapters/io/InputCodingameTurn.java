package fr.li212.codingame.tron.adapters.io;

import fr.li212.codingame.tron.adapters.grid.SquareCoordinate;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class InputCodingameTurn {

    private final Scanner in = new Scanner(System.in);

    public Set<PlayerContext> get() {
        final Set<PlayerContext> playerContexts = new HashSet<>();
        int N = in.nextInt();
        int P = in.nextInt();
        for (int i = 0; i < N; i++) {
            int X0 = in.nextInt();
            int Y0 = in.nextInt();
            int X1 = in.nextInt();
            int Y1 = in.nextInt();
            final PlayerContext currentContext = new PlayerContext(
                    new PlayerIdentifier(i),
                    new SquareCoordinate(X0, Y0),
                    new SquareCoordinate(X1, Y1),
                    i == P);
            playerContexts.add(currentContext);
        }
        playerContexts.forEach(System.err::println);
        return playerContexts;
    }
}
