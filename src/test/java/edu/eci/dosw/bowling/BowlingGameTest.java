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
            // Simulamos un juego completo de 20 tiros normales (2 tiros de 0 por cada uno de los 10 frames)
            for (int i = 0; i < 20; i++) {
                game.roll(0);
            }
            // Tiro adicional número 21 cuando el juego ya terminó
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

            // El siguiente tiro debe iniciar el segundo frame
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
            // 9 frames normales con 0 pinos (18 tiros)
            for (int i = 0; i < 18; i++) {
                game.roll(0);
            }
            // Frame 10: Strike + 2 tiros bonus
            assertDoesNotThrow(() -> {
                game.roll(10); // tiro 19 (strike en frame 10)
                game.roll(10); // tiro 20 (bonus 1)
                game.roll(10); // tiro 21 (bonus 2)
            });

            assertEquals(10, game.getFrames().size());
            Frame tenthFrame = game.getFrames().get(9);
            assertEquals(3, tenthFrame.getRolls().size());
        }
    }
}
