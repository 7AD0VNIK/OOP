package ru.nsu.ksadov.recbook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

/**
 * Электронная зачётная книжка студента ФИТ.
 * Хранит итоговые оценки и предоставляет методы анализа успеваемости.
 */
public class RecordBook {

    private final List<Grade> grades = new ArrayList<>();
    private boolean isPaid;
    private Integer diplomaMark;

    /**
     * Создаёт зачётную книжку.
     */
    public RecordBook(boolean isPaid) {
        this.isPaid = isPaid;
        this.diplomaMark = null;
    }

    /**
     * Добавляет итоговую оценку.
     */
    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    /**
     * Устанавливает оценку за дипломную работу.
     */
    public void setDiplomaMark(Integer mark) {
        this.diplomaMark = mark;
    }

    /**
     * @return оценка за дипломную работу или null
     */
    public Integer getDiplomaMark() {
        return diplomaMark;
    }

    /**
     * @return true — студент обучается на платной основе
     */
    public boolean isPaid() {
        return isPaid;
    }

    /**
     * Вычисляет текущий средний балл по всем числовым оценкам.
     * Учитываются только экзамены и дифференцированные зачёты.
     * @return средний балл или NaN если оценок нет
     */
    public double avarageMark() {
        IntSummaryStatistics stats = grades.stream()
                .filter(Grade::isNumMark)
                .filter(g -> g.getMark() >= 2).
                mapToInt(Grade::getMark)
                .summaryStatistics();

        if (stats.getCount() == 0) {
            return Double.NaN;
        }

        return (double) stats.getSum() / stats.getCount();
    }

    /**
     * Проверяет возможность перевода с платной формы на бюджет.
     * @return true — если в двух последних сессиях нет оценок «3»
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
        int prevSem = maxSem > 1 ? maxSem - 1 : maxSem;

        return grades.stream()
                .filter(g -> g.getSemester() == maxSem || g.getSemester() == prevSem)
                .filter(g -> g.getType() == GradeType.EXAM)
                .noneMatch(g -> g.getMark() == 3);
    }

    /**
     * Проверяет возможность получения красного диплома.
     * @return true — если >=75% оценок «5», нет «3», дипломная работа «5».
     */
    public boolean canGetRedDiploma() {
        List<Grade> finals = grades.stream()
                .filter(g ->
                        g.getType() == GradeType.EXAM ||
                                g.getType() == GradeType.DIFF_CREDIT
                )
                .collect(Collectors.toList());

        // Добавляем дипломную работу как полноценную итоговую оценку
        if (diplomaMark != null) {
            finals.add(new Grade("Diploma", Integer.MAX_VALUE, GradeType.DIPLOMA_WORK,
                    diplomaMark));
        }

        if (finals.isEmpty()) {
            return false;
        }

        long total = finals.size();
        long fives = finals.stream().filter(g -> g.getMark() == 5).count();
        boolean ratOk = ((double) fives) / total >= 0.75;

        boolean noLowMarks = finals.stream().noneMatch(g -> g.getMark() <= 3);

        boolean diplomaOk = diplomaMark == null || diplomaMark == 5;

        return ratOk && noLowMarks && diplomaOk;
    }

    /**
     * Проверяет возможность получения повышенной стипендии за семестр.
     * @param semester номер семестра
     * @return true — если все оценки ≥4 (зачёты — зачтено/4/5)
     */
    public boolean canGetIncreaseScholarship(int semester) {
        List<Grade> semGrades = grades.stream()
                .filter(g -> g.getSemester() == semester)
                .collect(Collectors.toList());

        if (semGrades.isEmpty()) {
            return false;
        }

        boolean allGood = semGrades.stream().allMatch(g -> {
            if (g.getType() == GradeType.EXAM || g.getType() == GradeType.DIFF_CREDIT) {
                return g.getMark() >= 4;
            }
            if (g.getType() == GradeType.CREDIT) {
                return g.getMark() == 0 || g.getMark() >= 4;
            }
            return true;
        });

        return allGood;
    }

    /**
     * Выводит все оценки в порядке семестров.
     */
    public void printAllGrades() {
        grades.stream().sorted(Comparator.comparingInt(Grade::getSemester))
                .forEach(System.out::println);
    }
}
