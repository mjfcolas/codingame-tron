package fr.li212.codingame.tron.domain;

public enum ScoreCoefficients {
    BEST_VORONOI(1);

    final int coefficient;

    ScoreCoefficients(final int coefficient) {
        this.coefficient = coefficient;
    }

    public int getCoefficient() {
        return coefficient;
    }
}
