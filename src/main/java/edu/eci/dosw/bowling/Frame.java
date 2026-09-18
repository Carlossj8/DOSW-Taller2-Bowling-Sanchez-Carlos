package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un frame con sus tiros en un juego de Bowling.
 */
public class Frame {

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

        if (!isTenthFrame) {
            if (rolls.isEmpty()) {
                rolls.add(pins);
                if (pins == 10) {
                    this.type = FrameType.STRIKE;
                }
            } else {
                if (rolls.get(0) + pins > 10) {
                    throw new IllegalArgumentException("Dos tiros en un frame normal no pueden sumar mas de 10 pinos");
                }
                rolls.add(pins);
                if (rolls.get(0) + pins == 10) {
                    this.type = FrameType.SPARE;
                } else {
                    this.type = FrameType.NORMAL;
                }
            }
        } else {
            if (rolls.isEmpty()) {
                rolls.add(pins);
            } else if (rolls.size() == 1) {
                if (rolls.get(0) < 10 && rolls.get(0) + pins > 10) {
                    throw new IllegalArgumentException("La suma de los dos primeros tiros en el frame 10 no puede superar 10 sin strike");
                }
                rolls.add(pins);
            } else if (rolls.size() == 2) {
                if (rolls.get(0) == 10 && rolls.get(1) < 10 && rolls.get(1) + pins > 10) {
                    throw new IllegalArgumentException("Los tiros bonus no pueden derribar mas de 10 pinos en un set");
                }
                rolls.add(pins);
            }
        }
    }

    public boolean isComplete() {
        if (isTenthFrame) {
            if (rolls.size() < 2) {
                return false;
            }
            if (rolls.get(0) == 10 || rolls.get(0) + rolls.get(1) == 10) {
                return rolls.size() == 3;
            }
            return rolls.size() == 2;
        } else {
            if (rolls.isEmpty()) {
                return false;
            }
            if (rolls.get(0) == 10) {
                return true;
            }
            return rolls.size() == 2;
        }
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
