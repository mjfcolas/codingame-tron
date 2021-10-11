package fr.li212.codingame.tron.domain.move;

import fr.li212.codingame.tron.domain.ScoreCoefficients;

public class ScoredMove implements Comparable<ScoredMove> {
    private final Move move;
    private final float voronoiScore;
    private final int numberOfLibertiesAfter;


    public ScoredMove(
            final Move move,
            final float voronoiScore,
            final int numberOfLibertiesAfter) {
        this.move = move;
        this.voronoiScore = voronoiScore;
        this.numberOfLibertiesAfter = numberOfLibertiesAfter;
    }

    public Move getMove() {
        return move;
    }

    public Integer getScore() {
        return (int) (voronoiScore * ScoreCoefficients.BEST_VORONOI.getCoefficient())
                + (numberOfLibertiesAfter * ScoreCoefficients.LEAST_NUMBER_OF_LIBERTIES.getCoefficient());
    }

    @Override
    public int compareTo(final ScoredMove toCompare) {
        return -getScore().compareTo(toCompare.getScore());
    }

    @Override
    public String toString() {
        return "ScoredMove{" +
                "move=" + move +
                ", voronoi=" + voronoiScore +
                ", liberties=" + numberOfLibertiesAfter +
                ", score=" + this.getScore() +
                '}';
    }
}
