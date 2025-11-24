package ru.nsu.ksadov.recbook;

/**
 * Класс, представляющий одну итоговую оценку по дисциплине.
 */
public class Grade {
    private final String subjectName;
    private final int semester;
    private final GradeType type;
    private final int mark;

    /**
     * Объект класса Grade.
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

    /** @return семестр */
    public int getSemester() {
        return semester;
    }

    /** @return тип контроля */
    public GradeType getType() {
        return type;
    }

    /** @return оценка */
    public int getMark() {
        return mark;
    }

    /**
     * @return true — если оценка числовая (экзамен или дифф. зачёт)
     */
    public boolean isNumMark() {
        return  (type == GradeType.EXAM || type == GradeType.DIFF_CREDIT);
    }

    /**
     * @return строковое представление.
     */
    @Override
    public String toString() {
        return (subjectName + ": семестр " + semester + ", " + type + " - " + mark);
    }
}
