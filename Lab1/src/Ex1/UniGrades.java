package Ex1;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class UniGrades {
    public int[] insufficientGrades(final int[] grades) {
        int[] insufficientGradesArray = new int[0];
        for (final int grade : grades) {
            validateGrade(grade);
            if (grade < 40) {
                insufficientGradesArray = addToArray(insufficientGradesArray, grade);
            }
        }
        return insufficientGradesArray;
    }

    public int[] addToArray(final int[] oldArray, final int newValue) {
        validateGrade(newValue);
        int[] newArray = new int[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[oldArray.length] = newValue;
        return newArray;
    }

    public String average(final int[] grades) {
        if (grades.length == 0) {
            throw new IllegalArgumentException("Grades array can't be empty.");
        }
        int sum = 0;
        for (final int grade : grades) {
            validateGrade(grade);
            sum += grade;
        }
        final DecimalFormat df = new DecimalFormat("0.00");
        double avg = (double) sum/grades.length;
        return df.format(avg);
        //return avg;

    }

    public int[] roundAllGrades(int[] grades) {
        int[] roundedGrades = new int[0];
        for (final int grade : grades) {
            validateGrade(grade);
            roundedGrades = addToArray(roundedGrades, round(grade));
        }
        return roundedGrades;
    }

    public int round(int grade) {
        validateGrade(grade);
        if (grade < 38) {
            return grade;
        }
        if (grade % 5 > 2) {
            return grade - grade % 5 + 5;
        }
        return grade;
    }

    public int maxGrade(final int[] grades) {
        if (grades.length == 0) {
            throw new IllegalArgumentException("Grades can not be empty in this case.");
        }
        int max = 0;
        for (final int grade : roundAllGrades(grades)) {
            validateGrade(grade);
            if (grade > max) {
                max = grade;
            }
        }
        return max;
    }

    private void validateGrade(final int grade) {
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade not valid");
        }
    }
}
