package ru.nsu.ksadov.recbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тесты для класса RecordBook.
 */
public class RecordBookTest {

    @Test
    void testAverageMark() {
        RecordBook rb = new RecordBook(false, 10);
        rb.addGrade(new Grade("Math", 1, GradeType.EXAM, 5));
        rb.addGrade(new Grade("Algo", 1, GradeType.DIFF_CREDIT, 4));
        assertEquals(4.5, rb.avarageMark());
    }

    @Test
    void testSwapToBudgetAllowed() {
        RecordBook rb = new RecordBook(true, 10);
        rb.addGrade(new Grade("Math", 2, GradeType.EXAM, 4));
        rb.addGrade(new Grade("Algo", 2, GradeType.DIFF_CREDIT, 4));
        rb.addGrade(new Grade("History", 1, GradeType.EXAM, 5));

        assertTrue(rb.isRealSwapToBudget(),
                "Нет троек по экзаменам в двух последних сессиях");
    }

    @Test
    void testSwapToBudgetFailsBecauseExam3() {
        RecordBook rb = new RecordBook(true, 10);
        rb.addGrade(new Grade("Math", 3, GradeType.EXAM, 3));
        assertFalse(rb.isRealSwapToBudget());
    }

    @Test
    void testRedDiplomaOk() {
        RecordBook rb = new RecordBook(false, 4);

        rb.addGrade(new Grade("Math", 1, GradeType.EXAM, 5));
        rb.addGrade(new Grade("Algo", 1, GradeType.DIFF_CREDIT, 5));
        rb.addGrade(new Grade("OOP", 2, GradeType.EXAM, 5));
        rb.setDiplomaMark(5);

        assertTrue(rb.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaFailsBecauseOf3() {
        RecordBook rb = new RecordBook(false, 10);
        rb.addGrade(new Grade("Math", 1, GradeType.EXAM, 3));
        rb.setDiplomaMark(5);

        assertFalse(rb.canGetRedDiploma(),
                "Тройка навсегда запрещает красный диплом");
    }

    @Test
    void redDiplomaPossibleInFuture() {
        RecordBook rb = new RecordBook(false, 20);

        rb.addGrade(new Grade("Math", 1, GradeType.EXAM, 4));
        rb.addGrade(new Grade("OOP", 1, GradeType.EXAM, 4));
        rb.addGrade(new Grade("History", 1, GradeType.EXAM, 4));
        rb.addGrade(new Grade("Physics", 1, GradeType.EXAM, 4));

        assertTrue(rb.canGetRedDiploma(),
                "Студент должен иметь возможность получить красный диплом," +
                        " если в будущем всё сдаст на 5");
    }

    @Test
    void testIncreaseScholarshipAllowed() {
        RecordBook rb = new RecordBook(false, 10);
        rb.addGrade(new Grade("Math", 4, GradeType.EXAM, 5));
        rb.addGrade(new Grade("Algo", 4, GradeType.DIFF_CREDIT, 4));
        rb.addGrade(new Grade("PE", 4, GradeType.CREDIT, 0));
        assertTrue(rb.canGetIncreaseScholarship(4));
    }

    @Test
    void testIncreaseScholarshipFails() {
        RecordBook rb = new RecordBook(false, 10);
        rb.addGrade(new Grade("Math", 4, GradeType.EXAM, 3));
        assertFalse(rb.canGetIncreaseScholarship(4));
    }
}
