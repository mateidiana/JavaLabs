package Ex1;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        UniGrades gradeProcessor = new UniGrades();
        int[] grades = new int[]{10, 20, 30, 39, 47, 48, 60};

        System.out.println("Insufficient grades: " + Arrays.toString(gradeProcessor.insufficientGrades(grades)));

        System.out.println("Sufficient grades: " + Arrays.toString(gradeProcessor.sufficientGrades(grades)));

        System.out.println("Grades average: " + gradeProcessor.average(grades));

        System.out.println("Round all grades: " + Arrays.toString(gradeProcessor.roundAllGrades(grades)));

        System.out.println("Round a grade: " + gradeProcessor.round(93));

        System.out.println("Add item to array: " + Arrays.toString(gradeProcessor.addToArray(grades, 99)));

        System.out.println("Max grade: " + gradeProcessor.maxGrade(grades));
    }
}
