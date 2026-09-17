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
        rolls.add(pins);
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
