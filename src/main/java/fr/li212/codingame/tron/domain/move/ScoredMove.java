package fr.li212.codingame.tron.domain.move;

import fr.li212.codingame.tron.domain.ScoreCoefficients;

public class ScoredMove implements Comparable<ScoredMove> {
    private final Move move;
    private final float voronoiScore;
    private final float opponentScoresSum;
    private final int numberOfLibertiesAfter;


    public ScoredMove(
            final Move move,
            final float voronoiScore,
            final float opponentScoresSum,
            final int numberOfLibertiesAfter) {
        this.move = move;
        this.voronoiScore = voronoiScore;
        this.opponentScoresSum = opponentScoresSum;
        this.numberOfLibertiesAfter = numberOfLibertiesAfter;
    }

    public Move getMove() {
        return move;
    }

    public Integer getScore() {
        return (int) (voronoiScore * ScoreCoefficients.PLAYER_VORONOI.getCoefficient())
                + (int) (opponentScoresSum * ScoreCoefficients.OPPONENT_SCORES.getCoefficient())
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
