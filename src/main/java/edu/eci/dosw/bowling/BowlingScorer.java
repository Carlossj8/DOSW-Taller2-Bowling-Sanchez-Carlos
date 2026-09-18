package edu.eci.dosw.bowling;

import java.util.List;

/**
 * Calculador sin estado para el puntaje total de un juego de Bowling.
 */
public class BowlingScorer {

    public static final int TOTAL_FRAMES = 10;
    public static final int STRIKE_PINS = 10;
    public static final int SPARE_PINS = 10;

    /**
     * Calcula el puntaje total a partir de la lista de frames jugados.
     *
     * @param frames Lista de los frames del juego
     * @return Puntaje total calculado
     */
    public int calculate(List<Frame> frames) {
        if (frames == null || frames.isEmpty()) {
            return 0;
        }

        int totalScore = 0;
        int framesToScore = Math.min(frames.size(), TOTAL_FRAMES);

        for (int i = 0; i < framesToScore; i++) {
            Frame frame = frames.get(i);

            if (frame.isTenthFrame() || i == TOTAL_FRAMES - 1) {
                totalScore += sumOfRolls(frame);
            } else if (frame.isStrike()) {
                totalScore += STRIKE_PINS + calculateStrikeBonus(frames, i);
            } else if (frame.isSpare()) {
                totalScore += SPARE_PINS + calculateSpareBonus(frames, i);
            } else {
                totalScore += sumOfRolls(frame);
            }
        }

        return totalScore;
    }

    private int calculateStrikeBonus(List<Frame> frames, int frameIndex) {
        if (frameIndex + 1 >= frames.size()) {
            return 0;
        }

        Frame nextFrame = frames.get(frameIndex + 1);
        List<Integer> nextRolls = nextFrame.getRolls();

        if (nextRolls.size() >= 2) {
            return nextRolls.get(0) + nextRolls.get(1);
        }

        int bonus = nextRolls.isEmpty() ? 0 : nextRolls.get(0);
        if (frameIndex + 2 < frames.size()) {
            List<Integer> secondNextRolls = frames.get(frameIndex + 2).getRolls();
            if (!secondNextRolls.isEmpty()) {
                bonus += secondNextRolls.get(0);
            }
        }
        return bonus;
    }

    private int calculateSpareBonus(List<Frame> frames, int frameIndex) {
        if (frameIndex + 1 >= frames.size()) {
            return 0;
        }
        List<Integer> nextRolls = frames.get(frameIndex + 1).getRolls();
        return nextRolls.isEmpty() ? 0 : nextRolls.get(0);
    }

    private int sumOfRolls(Frame frame) {
        int sum = 0;
        for (int roll : frame.getRolls()) {
            sum += roll;
        }
        return sum;
    }
}
