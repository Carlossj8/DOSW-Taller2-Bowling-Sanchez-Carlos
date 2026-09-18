package edu.eci.dosw.bowling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Frame - Pruebas Unitarias de Estructura y Reglas Internas")
class FrameTest {

    @Test
    @DisplayName("Constructor por defecto crea un frame estandar tipo NORMAL")
    void defaultConstructor_createsStandardNormalFrame() {
        Frame frame = new Frame();
        assertFalse(frame.isTenthFrame());
        assertEquals(FrameType.NORMAL, frame.getType());
        assertTrue(frame.getRolls().isEmpty());
    }

    @Test
    @DisplayName("Modificar el tipo de frame con setType")
    void setType_updatesFrameType() {
        Frame frame = new Frame();
        frame.setType(FrameType.SPARE);
        assertEquals(FrameType.SPARE, frame.getType());
    }

    @Test
    @DisplayName("Intentar agregar un tiro a un frame ya completo lanza IllegalStateException")
    void addRollToCompletedFrame_throwsIllegalStateException() {
        Frame frame = new Frame(false);
        frame.addRoll(10); // Strike completa el frame
        assertTrue(frame.isComplete());

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> frame.addRoll(5)
        );
        assertNotNull(ex.getMessage());
    }

    @Test
    @DisplayName("Frame 10: dos primeros tiros sin strike que suman > 10 lanzan IllegalArgumentException")
    void tenthFrame_twoRollsWithoutStrikeExceedTen_throwsIllegalArgumentException() {
        Frame tenthFrame = new Frame(true);
        tenthFrame.addRoll(6);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> tenthFrame.addRoll(5)
        );
        assertNotNull(ex.getMessage());
    }

    @Test
    @DisplayName("Frame 10: tras strike, dos tiros bonus que suman > 10 lanzan IllegalArgumentException")
    void tenthFrame_bonusRollsAfterStrikeExceedTen_throwsIllegalArgumentException() {
        Frame tenthFrame = new Frame(true);
        tenthFrame.addRoll(10); // Strike
        tenthFrame.addRoll(4);  // Primer bonus

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> tenthFrame.addRoll(7) // 4 + 7 = 11 > 10
        );
        assertNotNull(ex.getMessage());
    }
}
