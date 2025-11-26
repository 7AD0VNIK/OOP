package ru.nsu.ksadov.recbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тесты класса RecordBook.
 */
public class RecordBookTest {

    @Test
    void testAverageMark() {
        RecordBook rb = new RecordBook(false);
        rb.addGrade(new Grade("Math", 1, GradeType.EXAM, 5));
        rb.addGrade(new Grade("Algo", 1, GradeType.DIFF_CREDIT, 4));
        assertEquals(4.5, rb.avarageMark());
    }

    @Test
    void testSwapToBudgetAllowed() {
        RecordBook rb = new RecordBook(true);
        rb.addGrade(new Grade("Math", 2, GradeType.EXAM, 4));
        rb.addGrade(new Grade("Algo", 2, GradeType.DIFF_CREDIT, 3));
        assertTrue(rb.isRealSwapToBudget());
    }

    @Test
    void testSwapToBudgetFailsBecauseExam3() {
        RecordBook rb = new RecordBook(true);
        rb.addGrade(new Grade("Math", 3, GradeType.EXAM, 3));
        assertFalse(rb.isRealSwapToBudget());
    }

    @Test
    void testRedDiplomaOk() {
        RecordBook rb = new RecordBook(false);
        rb.addGrade(new Grade("Math", 1, GradeType.EXAM, 5));
        rb.addGrade(new Grade("Algo", 1, GradeType.DIFF_CREDIT, 5));
        rb.addGrade(new Grade("OOP", 2, GradeType.EXAM, 4));
        rb.setDiplomaMark(5);
        assertTrue(rb.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaFailsBecauseOf3() {
        RecordBook rb = new RecordBook(false);
        rb.addGrade(new Grade("Math", 1, GradeType.EXAM, 3));
        rb.setDiplomaMark(5);
        assertFalse(rb.canGetRedDiploma());
    }

    @Test
    void testIncreaseScholarshipAllowed() {
        RecordBook rb = new RecordBook(false);
        rb.addGrade(new Grade("Math", 4, GradeType.EXAM, 5));
        rb.addGrade(new Grade("Algo", 4, GradeType.DIFF_CREDIT, 4));
        rb.addGrade(new Grade("PE", 4, GradeType.CREDIT, 0));
        assertTrue(rb.canGetIncreaseScholarship(4));
    }

    @Test
    void testIncreaseScholarshipFails() {
        RecordBook rb = new RecordBook(false);
        rb.addGrade(new Grade("Math", 4, GradeType.EXAM, 3));
        assertFalse(rb.canGetIncreaseScholarship(4));
    }
}
