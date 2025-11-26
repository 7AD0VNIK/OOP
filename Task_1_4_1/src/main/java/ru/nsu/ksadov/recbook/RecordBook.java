package ru.nsu.ksadov.recbook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalInt;

/**
 * Электронная зачётная книжка студента.
 * Хранит итоговые оценки и предоставляет методы анализа успеваемости.
 */
public class RecordBook {

    /** Список всех итоговых оценок. */
    private final List<Grade> grades = new ArrayList<>();

    /** Признак платной формы обучения. */
    private final boolean isPaid;

    /** Итоговая оценка за дипломную работу (если есть). */
    private Integer diplomaMark;

    /**
     * Создаёт новую зачётную книжку.
     *
     * @param isPaid true, если студент учится на платной основе
     */
    public RecordBook(boolean isPaid) {
        this.isPaid = isPaid;
    }

    /**
     * Добавляет новую итоговую оценку.
     *
     * @param grade объект Grade
     */
    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    /**
     * Устанавливает оценку за дипломную работу.
     *
     * @param mark оценка (обычно 4 или 5)
     */
    public void setDiplomaMark(Integer mark) {
        this.diplomaMark = mark;
    }

    /**
     * Возвращает оценку за дипломную работу.
     *
     * @return оценка или null
     */
    public Integer getDiplomaMark() {
        return diplomaMark;
    }

    /**
     * Проверяет, учится ли студент на платной основе.
     *
     * @return true, если форма обучения платная
     */
    public boolean isPaid() {
        return isPaid;
    }

    /**
     * Вычисляет средний балл по всем числовым оценкам
     * (экзамены + дифференцированные зачёты).
     *
     * @return средний балл или NaN, если числовых оценок нет
     */
    public double avarageMark() {
        IntSummaryStatistics stats = grades.stream()
                .filter(Grade::isNumMark)
                .filter(g -> g.getMark() >= 2)
                .mapToInt(Grade::getMark)
                .summaryStatistics();

        if (stats.getCount() == 0) {
            return Double.NaN;
        }
        return (double) stats.getSum() / stats.getCount();
    }

    /**
     * Проверяет возможность перевода на бюджет.
     * Условие: в двух последних сессиях нет «3» по экзаменам.
     *
     * @return true, если студент может перейти на бюджет
     */
    public boolean isRealSwapToBudget() {
        if (!isPaid) {
            return false;
        }

        OptionalInt maxSemOpt = grades.stream()
                .mapToInt(Grade::getSemester)
                .max();

        if (maxSemOpt.isEmpty()) {
            return false;
        }

        int last = maxSemOpt.getAsInt();
        int prev = Math.max(1, last - 1);

        return grades.stream()
                .filter(g -> g.getSemester() == last || g.getSemester() == prev)
                .filter(g -> g.getType() == GradeType.EXAM)
                .noneMatch(g -> g.getMark() == 3);
    }

    /**
     * Проверяет возможность получения красного диплома.
     * Требования:
     *  • не менее 75% оценок — «5»;
     *  • отсутствие оценок «3»;
     *  • диплом — «5».
     *
     * @return true, если красный диплом возможен
     */
    public boolean canGetRedDiploma() {

        List<Grade> finals = grades.stream()
                .filter(g -> g.getType() == GradeType.EXAM
                        || g.getType() == GradeType.DIFF_CREDIT)
                .toList();

        // Добавляем диплом, если указан
        if (diplomaMark != null) {
            finals = new ArrayList<>(finals);
            finals.add(new Grade(
                    "Diploma",
                    Integer.MAX_VALUE,
                    GradeType.DIPLOMA_WORK,
                    diplomaMark
            ));
        }

        if (finals.isEmpty()) {
            return false;
        }

        long total = finals.size();
        long fives = finals.stream().filter(g -> g.getMark() == 5).count();

        boolean ratioOk = (double) fives / total >= 0.75;
        boolean hasNoThree = finals.stream().noneMatch(g -> g.getMark() == 3);
        boolean diplomaOk = diplomaMark == null || diplomaMark == 5;

        return ratioOk && hasNoThree && diplomaOk;
    }

    /**
     * Проверяет возможность получения повышенной стипендии
     * в указанном семестре.
     *
     * @param semester номер семестра
     * @return true, если студент удовлетворяет условиям
     */
    public boolean canGetIncreaseScholarship(int semester) {
        List<Grade> sem = new ArrayList<>();
        for (Grade g : grades) {
            if (g.getSemester() == semester) {
                sem.add(g);
            }
        }

        if (sem.isEmpty()) {
            return false;
        }

        for (Grade g : sem) {
            switch (g.getType()) {
                case EXAM:
                case DIFF_CREDIT:
                    if (g.getMark() < 4) {
                        return false;
                    }
                    break;
                case CREDIT:
                    if (g.getMark() != 0 && g.getMark() < 4) {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }

        return true;
    }

    /**
     * Печатает все оценки, сортируя по семестрам.
     */
    public void printAllGrades() {
        grades.stream()
                .sorted(Comparator.comparingInt(Grade::getSemester))
                .forEach(System.out::println);
    }
}
