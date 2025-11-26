package ru.nsu.ksadov.recbook;

/**
 * Класс, представляющий итоговую оценку по дисциплине.
 */
public class Grade {

    /** Название дисциплины. */
    private final String subjectName;

    /** Номер семестра. */
    private final int semester;

    /** Тип итогового контроля. */
    private final GradeType type;

    /** Числовая оценка (0 — зачёт). */
    private final int mark;

    /**
     * Создаёт новую запись об итоговой оценке.
     *
     * @param subjectName название дисциплины
     * @param semester    номер семестра
     * @param type        тип контроля
     * @param mark        числовая оценка или 0
     */
    public Grade(String subjectName, int semester, GradeType type, int mark) {
        if (type == GradeType.CREDIT && mark != 0) {
            throw new IllegalArgumentException("Для зачёта оценка должна быть 0");
        }
        if ((type == GradeType.EXAM || type == GradeType.DIFF_CREDIT || type == GradeType.DIPLOMA_WORK)
                && (mark < 2 || mark > 5)) {
            throw new IllegalArgumentException("Оценка должна быть от 2 до 5");
        }
        this.subjectName = subjectName;
        this.semester = semester;
        this.type = type;
        this.mark = mark;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getSemester() {
        return semester;
    }

    public GradeType getType() {
        return type;
    }

    public int getMark() {
        return mark;
    }

    /**
     * Проверяет, является ли оценка числовой (экзамен или диффзачёт).
     *
     * @return true, если оценка числовая
     */
    public boolean isNumMark() {
        return type == GradeType.EXAM || type == GradeType.DIFF_CREDIT;
    }

    @Override
    public String toString() {
        return subjectName + ": семестр "
                + semester + ", " + type + " — " + mark;
    }
}
