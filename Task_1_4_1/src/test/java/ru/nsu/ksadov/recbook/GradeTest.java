package ru.nsu.ksadov.recbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
/**
 * Тесты класса Grade.
 */
public class GradeTest {

    @Test
    void testGradeFields() {
        Grade g = new Grade("Math", 2, GradeType.EXAM, 5);
        assertEquals("Math", g.getSubjectName());
        assertEquals(2, g.getSemester());
        assertEquals(GradeType.EXAM, g.getType());
        assertEquals(5, g.getMark());
    }

    @Test
    void testIsNumMarkExam() {
        Grade g = new Grade("Algo", 1, GradeType.EXAM, 4);
        assertTrue(g.isNumMark());
    }

    @Test
    void testIsNumMarkDiffCredit() {
        Grade g = new Grade("Algo", 1, GradeType.DIFF_CREDIT, 3);
        assertTrue(g.isNumMark());
    }

    @Test
    void testIsNumMarkCredit() {
        Grade g = new Grade("PE", 1, GradeType.CREDIT, 0);
        assertFalse(g.isNumMark());
    }

    @Test
    void testToStringNotEmpty() {
        Grade g = new Grade("Math", 2, GradeType.EXAM, 5);
        assertFalse(g.toString().isEmpty());
    }
}
