package fr.li212.codingame.tron.domain.move;

import fr.li212.codingame.tron.domain.ScoreCoefficients;

public class ScoredMove implements Comparable<ScoredMove> {
    private final Move move;
    private final Integer score;

    public ScoredMove(
            final Move move,
            final float voronoiScore) {
        this.move = move;
        this.score = (int)(voronoiScore * ScoreCoefficients.BEST_CONFLICTUAL_CELL.getCoefficient());
    }

    public Move getMove() {
        return move;
    }

    public Integer getScore() {
        return score;
    }

    @Override
    public int compareTo(final ScoredMove toCompare) {
        return -score.compareTo(toCompare.getScore());
    }

    @Override
    public String toString() {
        return "ScoredMove{" +
                "move=" + move +
                ", score=" + score +
                '}';
    }
}
