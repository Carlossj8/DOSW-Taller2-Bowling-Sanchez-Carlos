package edu.eci.dosw.bowling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BowlingGame - Pruebas del Motor de Juego")
class BowlingGameTest {

    private BowlingGame game;

    @BeforeEach
    void setUp() {
        game = new BowlingGame();
    }

    // Helper methods para pruebas
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

    @Nested
    @DisplayName("Modulo A: Validaciones y Registro de Tiros (roll)")
    class ModuleATests {

        @Test
        @DisplayName("A1: roll(0) - Primer tiro a cero no lanza excepcion y registra 0 pinos en el frame")
        void a1_rollZeroPins_registersInFrame() {
            game.roll(0);

            assertEquals(1, game.getFrames().size(), "Debe existir 1 frame iniciado");
            Frame currentFrame = game.getFrames().get(0);
            assertEquals(1, currentFrame.getRolls().size(), "El frame debe tener 1 tiro registrado");
            assertEquals(0, currentFrame.getRolls().get(0), "El tiro debe haber registrado 0 pinos");
        }

        @Test
        @DisplayName("A2: roll(-1) - Valor negativo lanza IllegalArgumentException")
        void a2_rollNegativePins_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> game.roll(-1)
            );
            assertNotNull(ex.getMessage());
        }

        @Test
        @DisplayName("A3: roll(11) - Valor mayor a 10 lanza IllegalArgumentException")
        void a3_rollMoreThanTenPins_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> game.roll(11)
            );
            assertNotNull(ex.getMessage());
        }

        @Test
        @DisplayName("A4: Dos tiros en un frame normal suman > 10 lanza IllegalArgumentException en el segundo tiro")
        void a4_rollsInFrameExceedTen_throwsIllegalArgumentException() {
            game.roll(7);
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> game.roll(6)
            );
            assertNotNull(ex.getMessage());
        }

        @Test
        @DisplayName("A5: roll() cuando el juego ya esta completo lanza IllegalStateException")
        void a5_rollWhenGameIsComplete_throwsIllegalStateException() {
            rollMany(20, 0);
            IllegalStateException ex = assertThrows(
                    IllegalStateException.class,
                    () -> game.roll(0)
            );
            assertNotNull(ex.getMessage());
        }

        @Test
        @DisplayName("A6: roll(10) - Detecta strike, marca el frame como STRIKE y avanza al siguiente frame")
        void a6_strike_marksFrameAsStrikeAndAdvances() {
            game.roll(10);

            assertEquals(1, game.getFrames().size());
            Frame firstFrame = game.getFrames().get(0);
            assertEquals(FrameType.STRIKE, firstFrame.getType());

            game.roll(4);
            assertEquals(2, game.getFrames().size());
        }

        @Test
        @DisplayName("A7: roll(5) + roll(5) - Detecta spare y marca el frame como SPARE")
        void a7_spare_marksFrameAsSpare() {
            game.roll(5);
            game.roll(5);

            assertEquals(1, game.getFrames().size());
            Frame firstFrame = game.getFrames().get(0);
            assertEquals(FrameType.SPARE, firstFrame.getType());
        }

        @Test
        @DisplayName("A8: Frame 10 con strike acepta hasta 3 tiros sin excepcion")
        void a8_tenthFrameWithStrike_acceptsThreeRolls() {
            rollMany(18, 0);
            assertDoesNotThrow(() -> {
                game.roll(10);
                game.roll(10);
                game.roll(10);
            });

            assertEquals(10, game.getFrames().size());
            Frame tenthFrame = game.getFrames().get(9);
            assertEquals(3, tenthFrame.getRolls().size());
        }
    }

    @Nested
    @DisplayName("Modulo C: Estado de Finalizacion del Juego (isComplete)")
    class ModuleCTests {

        @Test
        @DisplayName("C1: isComplete() al inicio del juego retorna false")
        void c1_isComplete_atGameStart_returnsFalse() {
            assertFalse(game.isComplete());
        }

        @Test
        @DisplayName("C2: isComplete() despues de 9 frames completos retorna false")
        void c2_isComplete_afterNineFrames_returnsFalse() {
            rollMany(18, 0);
            assertFalse(game.isComplete());
        }

        @Test
        @DisplayName("C3: 10 frames normales completos (sin strike/spare en frame 10) retorna true")
        void c3_isComplete_tenNormalFrames_returnsTrue() {
            rollMany(20, 0);
            assertTrue(game.isComplete());
        }

        @Test
        @DisplayName("C4: Spare en frame 10 + tiro bonus ejecutado retorna true")
        void c4_isComplete_tenthFrameSpareWithBonus_returnsTrue() {
            rollMany(18, 0);
            game.roll(5);
            game.roll(5);
            assertFalse(game.isComplete(), "No debe completarse antes del tiro de bonificacion");
            game.roll(7);
            assertTrue(game.isComplete(), "Debe completarse tras el tiro bonus");
        }

        @Test
        @DisplayName("C5: Strike en frame 10 + 2 tiros bonus ejecutados retorna true")
        void c5_isComplete_tenthFrameStrikeWithTwoBonuses_returnsTrue() {
            rollMany(18, 0);
            game.roll(10);
            assertFalse(game.isComplete(), "No debe completarse solo con el strike en frame 10");
            game.roll(10);
            assertFalse(game.isComplete(), "No debe completarse con 1 tiro bonus");
            game.roll(10);
            assertTrue(game.isComplete(), "Debe completarse con los 2 tiros bonus");
        }

        @Test
        @DisplayName("C6: Juego perfecto tras el 12º strike retorna true")
        void c6_isComplete_perfectGame_returnsTrue() {
            rollPerfectGame();
            assertTrue(game.isComplete());
        }
    }
}
