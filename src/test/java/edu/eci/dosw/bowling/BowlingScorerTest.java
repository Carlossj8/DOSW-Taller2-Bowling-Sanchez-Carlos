package edu.eci.dosw.bowling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BowlingScorer - Pruebas del Calculo de Puntuacion (calculate / score)")
class BowlingScorerTest {

    private BowlingGame game;
    private BowlingScorer scorer;

    @BeforeEach
    void setUp() {
        game = new BowlingGame();
        scorer = new BowlingScorer();
    }

    private void rollMany(int times, int pins) {
        for (int i = 0; i < times; i++) {
            game.roll(pins);
        }
    }

    private void rollPerfectGame() {
        for (int i = 0; i < 12; i++) {
            game.roll(10);
        }
    }

    private void rollAllSpares(int lastBonus) {
        for (int i = 0; i < 10; i++) {
            game.roll(5);
            game.roll(5);
        }
        game.roll(lastBonus);
    }

    @Test
    @DisplayName("B1: Juego con todos los tiros a 0 - score() == 0")
    void b1_gutterGame_scoresZero() {
        rollMany(20, 0);
        assertEquals(0, game.score());
        assertEquals(0, scorer.calculate(game.getFrames()));
    }

    @Test
    @DisplayName("B2: Juego sin strikes ni spares - suma directa de pinos")
    void b2_openGameWithoutStrikesOrSpares_scoresSumOfPins() {
        // 20 tiros de 1 pino cada uno = 20 puntos
        rollMany(20, 1);
        assertEquals(20, game.score());
    }

    @Test
    @DisplayName("B3: Spare en frame 1, primer tiro de frame 2 = 3 - calcula bono correctamente (10 + 3 = 13)")
    void b3_oneSpareInFirstFrame_scoresSpareBonus() {
        // Frame 1: 5 + 5 (spare) -> suma 10 + 3 = 13
        game.roll(5);
        game.roll(5);
        // Frame 2: 3 + 0 -> suma 3
        game.roll(3);
        game.roll(0);
        // Frames 3 a 10: 0 pinos (16 tiros restantes)
        rollMany(16, 0);

        // Total esperado: 13 + 3 = 16
        assertEquals(16, game.score());
    }

    @Test
    @DisplayName("B4: Strike en frame 1, luego roll(4) y roll(3) - calcula bono correctamente (10 + 4 + 3 = 17)")
    void b4_oneStrikeInFirstFrame_scoresStrikeBonus() {
        // Frame 1: 10 (strike) -> suma 10 + 4 + 3 = 17
        game.roll(10);
        // Frame 2: 4 + 3 -> suma 7
        game.roll(4);
        game.roll(3);
        // Frames 3 a 10: 0 pinos (16 tiros restantes)
        rollMany(16, 0);

        // Total esperado: 17 + 7 = 24
        assertEquals(24, game.score());
    }

    @Test
    @DisplayName("B5: Dos strikes consecutivos, luego roll(5) - bono del primer strike suma correctamente")
    void b5_consecutiveStrikes_scoreBonusesCorrectly() {
        // Frame 1: 10 (strike) -> suma 10 + 10 + 5 = 25
        game.roll(10);
        // Frame 2: 10 (strike) -> suma 10 + 5 + 2 = 17
        game.roll(10);
        // Frame 3: 5 + 2 -> suma 7
        game.roll(5);
        game.roll(2);
        // Frames 4 a 10: 0 pinos (14 tiros restantes)
        rollMany(14, 0);

        // Total esperado: 25 + 17 + 7 = 49
        assertEquals(49, game.score());
    }

    @Test
    @DisplayName("B6: Todos spares con ultimo tiro bonus en 5 - score() == 150")
    void b6_allSparesWithFiveBonus_scores150() {
        rollAllSpares(5);
        assertEquals(150, game.score());
    }

    @Test
    @DisplayName("B7: Juego perfecto con 12 strikes - score() == 300")
    void b7_perfectGame_scores300() {
        rollPerfectGame();
        assertEquals(300, game.score());
    }

    @Test
    @DisplayName("B8: score() antes de completar el juego lanza IllegalStateException")
    void b8_scoreBeforeGameIsComplete_throwsIllegalStateException() {
        rollMany(10, 0); // Solo 5 frames completados
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> game.score()
        );
        assertNotNull(ex.getMessage());
    }
}
