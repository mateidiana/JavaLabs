package Ex1;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        UniGrades gradeProcessor = new UniGrades();
        int[] grades = new int[]{10, 27, 37, 38, 39, 47, 48, 60};

        System.out.println("Insufficient grades: " + Arrays.toString(gradeProcessor.insufficientGrades(grades)));
        System.out.println("Average: " + gradeProcessor.average(grades));
        System.out.println("Round grades: " + Arrays.toString(gradeProcessor.roundAllGrades(grades)));
        System.out.println("Max grade: " + gradeProcessor.maxGrade(grades));
    }
}
