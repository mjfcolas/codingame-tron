package fr.li212.codingame.tron.domain;

public enum ScoreCoefficients {
    OUT_OF_GRID(-1000),
    BEST_VORONOI(10);

    final int coefficient;

    ScoreCoefficients(final int coefficient) {
        this.coefficient = coefficient;
    }

    public int getCoefficient() {
        return coefficient;
    }
}
