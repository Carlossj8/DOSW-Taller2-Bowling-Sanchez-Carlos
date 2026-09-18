package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un frame con sus tiros en un juego de Bowling.
 */
public class Frame {

    public static final int MAX_PINS = 10;
    public static final int STANDARD_FRAME_ROLLS = 2;
    public static final int TENTH_FRAME_BONUS_ROLLS = 3;

    private final List<Integer> rolls;
    private FrameType type;
    private final boolean isTenthFrame;

    public Frame(boolean isTenthFrame) {
        this.rolls = new ArrayList<>();
        this.type = isTenthFrame ? FrameType.TENTH : FrameType.NORMAL;
        this.isTenthFrame = isTenthFrame;
    }

    public Frame() {
        this(false);
    }

    public void addRoll(int pins) {
        if (isComplete()) {
            throw new IllegalStateException("El frame ya esta completo");
        }

        if (isTenthFrame) {
            addTenthFrameRoll(pins);
        } else {
            addStandardRoll(pins);
        }
    }

    private void addStandardRoll(int pins) {
        if (rolls.isEmpty()) {
            rolls.add(pins);
            if (pins == MAX_PINS) {
                this.type = FrameType.STRIKE;
            }
        } else {
            if (rolls.get(0) + pins > MAX_PINS) {
                throw new IllegalArgumentException("Dos tiros en un frame normal no pueden sumar mas de 10 pinos");
            }
            rolls.add(pins);
            if (rolls.get(0) + pins == MAX_PINS) {
                this.type = FrameType.SPARE;
            } else {
                this.type = FrameType.NORMAL;
            }
        }
    }

    private void addTenthFrameRoll(int pins) {
        if (rolls.size() == 1 && rolls.get(0) < MAX_PINS && rolls.get(0) + pins > MAX_PINS) {
            throw new IllegalArgumentException("La suma de los dos primeros tiros en el frame 10 no puede superar 10 sin strike");
        }
        if (rolls.size() == 2 && rolls.get(0) == MAX_PINS && rolls.get(1) < MAX_PINS && rolls.get(1) + pins > MAX_PINS) {
            throw new IllegalArgumentException("Los tiros bonus no pueden derribar mas de 10 pinos en un set");
        }
        rolls.add(pins);
    }

    public boolean isComplete() {
        if (isTenthFrame) {
            if (rolls.size() < STANDARD_FRAME_ROLLS) {
                return false;
            }
            return hasTenthFrameBonus() ? rolls.size() == TENTH_FRAME_BONUS_ROLLS : rolls.size() == STANDARD_FRAME_ROLLS;
        }

        return isStrike() || rolls.size() == STANDARD_FRAME_ROLLS;
    }

    public boolean isStrike() {
        return !rolls.isEmpty() && rolls.get(0) == MAX_PINS;
    }

    public boolean isSpare() {
        return rolls.size() >= STANDARD_FRAME_ROLLS && !isStrike() && (rolls.get(0) + rolls.get(1) == MAX_PINS);
    }

    private boolean hasTenthFrameBonus() {
        return rolls.get(0) == MAX_PINS || (rolls.get(0) + rolls.get(1) == MAX_PINS);
    }

    public List<Integer> getRolls() {
        return List.copyOf(rolls);
    }

    public FrameType getType() {
        return type;
    }

    public void setType(FrameType type) {
        this.type = type;
    }

    public boolean isTenthFrame() {
        return isTenthFrame;
    }
}
