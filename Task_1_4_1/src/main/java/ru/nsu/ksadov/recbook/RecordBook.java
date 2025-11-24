package ru.nsu.ksadov.recbook;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Электронная зачётная книжка студента.
 * Хранит итоговые оценки и предоставляет методы анализа успеваемости.
 */
public class RecordBook {

    private final List<Grade> grades = new ArrayList<>();
    private final boolean isPaid;
    private Integer diplomaMark;

    /**
     * Создаёт новую зачётную книжку.
     *
     * @param isPaid true — если студент учится на платной основе
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
     * @param mark оценка за диплом (обычно 4 или 5)
     */
    public void setDiplomaMark(Integer mark) {
        this.diplomaMark = mark;
    }

    /**
     * @return оценку за дипломную работу или null
     */
    public Integer getDiplomaMark() {
        return diplomaMark;
    }

    /**
     * @return true — если студент учится на платной основе
     */
    public boolean isPaid() {
        return isPaid;
    }

    /**
     * Вычисляет средний балл по всем числовым оценкам:
     * экзамены + дифференцированные зачёты.
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
     * Проверяет возможность перевода с платной формы на бюджет.
     * Требование: в двух последних сессиях нет оценок «3» по экзаменам.
     *
     * @return true — перевод возможен
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

        int maxSem = maxSemOpt.getAsInt();
        int prevSem = Math.max(1, maxSem - 1);

        return grades.stream()
                .filter(g -> g.getSemester() == maxSem || g.getSemester() == prevSem)
                .filter(g -> g.getType() == GradeType.EXAM)
                .noneMatch(g -> g.getMark() == 3);
    }

    /**
     * Проверяет, может ли студент получить красный диплом.
     * Условия:
     *  • не менее 75% оценок — «5»
     *  • нет оценок «3»
     *  • дипломная работа — «5»
     *
     * @return true — красный диплом возможен
     */
    public boolean canGetRedDiploma() {
        List<Grade> finals = grades.stream()
                .filter(g -> g.getType() == GradeType.EXAM
                        || g.getType() == GradeType.DIFF_CREDIT)
                .collect(Collectors.toList());

        if (diplomaMark != null) {
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

        boolean ratioOk = ((double) fives) / total >= 0.75;

        // Оценка «3» недопустима. 0 — зачёт, 4 и 5 — норм.
        boolean noThrees = finals.stream().noneMatch(g -> g.getMark() == 3);

        boolean diplomaOk = diplomaMark == null || diplomaMark == 5;

        return ratioOk && noThrees && diplomaOk;
    }

    /**
     * Проверяет возможность получения повышенной стипендии
     * за указанный семестр.
     *
     * Требования:
     *  • экзамены и диффзачёты — не ниже 4
     *  • зачёты: либо «зачтено» (0), либо 4 или 5
     *
     * @param semester номер семестра
     * @return true — если студент подходит под критерии
     */
    public boolean canGetIncreaseScholarship(int semester) {
        List<Grade> semGrades = grades.stream()
                .filter(g -> g.getSemester() == semester)
                .collect(Collectors.toList());

        if (semGrades.isEmpty()) {
            return false;
        }

        return semGrades.stream().allMatch(g -> {
            if (g.getType() == GradeType.EXAM
                    || g.getType() == GradeType.DIFF_CREDIT) {
                return g.getMark() >= 4;
            }
            if (g.getType() == GradeType.CREDIT) {
                return g.getMark() == 0 || g.getMark() >= 4;
            }
            return true; // прочие типы контроля (курсовая, практика)
        });
    }

    /**
     * Печатает все оценки, отсортированные по семестрам.
     */
    public void printAllGrades() {
        grades.stream()
                .sorted(Comparator.comparingInt(Grade::getSemester))
                .forEach(System.out::println);
    }
}
