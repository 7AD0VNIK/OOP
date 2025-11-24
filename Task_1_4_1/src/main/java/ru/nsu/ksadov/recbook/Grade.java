package ru.nsu.ksadov.recbook;

/**
 * Класс, представляющий итоговую оценку по дисциплине.
 */
public class Grade {

    private final String subjectName;
    private final int semester;
    private final GradeType type;
    private final int mark;

    /**
     * Создаёт новую запись об итоговой оценке.
     *
     * @param subjectName название предмета
     * @param semester    номер семестра
     * @param type        тип итогового контроля
     * @param mark        оценка (для зачёта 0 означает «зачтено»)
     */
    public Grade(String subjectName, int semester, GradeType type, int mark) {
        this.subjectName = subjectName;
        this.semester = semester;
        this.type = type;
        this.mark = mark;
    }

    /** @return название дисциплины */
    public String getSubjectName() {
        return subjectName;
    }

    /** @return номер семестра */
    public int getSemester() {
        return semester;
    }

    /** @return тип контроля */
    public GradeType getType() {
        return type;
    }

    /** @return оценку (числовую или 0 для зачёта) */
    public int getMark() {
        return mark;
    }

    /**
     * @return true — если оценка числовая (экзамен или диффзачёт)
     */
    public boolean isNumMark() {
        return type == GradeType.EXAM || type == GradeType.DIFF_CREDIT;
    }

    @Override
    public String toString() {
        return subjectName
                + ": семестр " + semester
                + ", " + type
                + " — " + mark;
    }
}
