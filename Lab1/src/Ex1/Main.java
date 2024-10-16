package Ex1;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        UniGrades gradeProcessor = new UniGrades();
        int[] grades = new int[]{29, 37, 38, 41, 84, 67};


        System.out.println("Insufficient grades: " + Arrays.toString(gradeProcessor.insufficientGrades(gradeProcessor.roundAllGrades(grades))));
        System.out.println("Average: " + gradeProcessor.average(grades));
        System.out.println("Round grades: " + Arrays.toString(gradeProcessor.roundAllGrades(grades)));
        System.out.println("Max grade: " + gradeProcessor.maxGrade(grades));
    }
}
