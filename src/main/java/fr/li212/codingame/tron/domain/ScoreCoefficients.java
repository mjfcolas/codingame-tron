package fr.li212.codingame.tron.domain;

public enum ScoreCoefficients {
    PLAYER_VORONOI(1000),
    OPPONENT_VORONOI_SUM(-10),
    LEAST_NUMBER_OF_LIBERTIES(-1);

    final int coefficient;

    ScoreCoefficients(final int coefficient) {
        this.coefficient = coefficient;
    }

    public int getCoefficient() {
        return coefficient;
    }
}
