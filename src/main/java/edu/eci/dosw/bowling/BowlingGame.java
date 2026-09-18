package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de un juego de Bowling para un jugador.
 * Un juego tiene exactamente 10 frames.
 */
public class BowlingGame {

    private static final int MAX_FRAMES = 10;
    private final List<Frame> frames;
    private int currentFrame;

    public BowlingGame() {
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
    }

    /**
     * Registra pinos derribados. Lanza IllegalArgumentException si pines < 0 o > 10.
     * Lanza IllegalStateException si el juego ya termino.
     */
    public void roll(int pins) {
        if (pins < 0 || pins > 10) {
            throw new IllegalArgumentException("Los pinos deben estar entre 0 y 10: " + pins);
        }
        if (isComplete()) {
            throw new IllegalStateException("El juego ya esta completo, no se pueden registrar mas tiros");
        }

        Frame current = getCurrentOrCreateFrame();
        current.addRoll(pins);
        if (current.isComplete()) {
            currentFrame++;
        }
    }

    private Frame getCurrentOrCreateFrame() {
        if (frames.isEmpty() || frames.get(frames.size() - 1).isComplete()) {
            boolean isTenth = (frames.size() == MAX_FRAMES - 1);
            Frame newFrame = new Frame(isTenth);
            frames.add(newFrame);
            return newFrame;
        }
        return frames.get(frames.size() - 1);
    }

    /**
     * Puntaje total. Lanza IllegalStateException si el juego no esta completo.
     */
    public int score() {
        if (!isComplete()) {
            throw new IllegalStateException("El juego no esta completo");
        }
        return new BowlingScorer().calculate(frames);
    }

    /**
     * true cuando los 10 frames han sido completados.
     */
    public boolean isComplete() {
        return frames.size() == MAX_FRAMES && frames.get(MAX_FRAMES - 1).isComplete();
    }

    public List<Frame> getFrames() {
        return List.copyOf(frames);
    }
}
