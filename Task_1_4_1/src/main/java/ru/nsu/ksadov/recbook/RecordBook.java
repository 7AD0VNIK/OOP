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

    /** Общее количество итоговых оценок, которое студент должен получить за всё обучение */
    private final int expectedTotalFinalGrades;

    /**
     * Создаёт новую зачётную книжку.
     *
     * @param isPaid true, если студент учится на платной основе
     */
    public RecordBook(boolean isPaid, int expectedTotalFinalGrades) {
        this.isPaid = isPaid;
        this.expectedTotalFinalGrades = expectedTotalFinalGrades;
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
     * Проверяет возможность получения красного диплома с учётом того,
     * что ВСЕ будущие оценки могут быть «5».
     * Требования:
     *  • не менее 75% оценок — «5» (включая будущие);
     *  • отсутствие «3»;
     *  • диплом — «5».
     */
    public boolean canGetRedDiploma() {

        List<Grade> finals = grades.stream()
                .filter(g -> g.getType() == GradeType.EXAM
                        || g.getType() == GradeType.DIFF_CREDIT)
                .toList();

        long currentTotal = finals.size();
        long currentFives = finals.stream().filter(g -> g.getMark() == 5).count();

        // нет троек
        boolean hasThree = finals.stream().anyMatch(g -> g.getMark() == 3);
        if (hasThree) {
            return false;
        }

        // диплом
        if (diplomaMark != null && diplomaMark != 5) {
            return false;
        }

        // Если диплом уже стоит, учитываем его как оценку
        long diplomaAdd = (diplomaMark != null ? 1 : 0);
        long totalWithDiploma = currentTotal + diplomaAdd;

        long fivesWithDiploma = currentFives + (diplomaMark != null && diplomaMark == 5 ? 1 : 0);

        // ожидаемое кол-во оценок
        long remaining = expectedTotalFinalGrades - totalWithDiploma;

        if (remaining < 0) {
            remaining = 0;
        }

        long maxPossibleFives = fivesWithDiploma + remaining;

        double maxPossibleRatio = (double) maxPossibleFives / expectedTotalFinalGrades;

        return maxPossibleRatio >= 0.75;
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
