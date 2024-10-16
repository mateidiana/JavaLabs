package Ex3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        NumberProcessor np = new NumberProcessor();

        int[] nr1 = new int[]{1, 3, 0, 0};
        int[] nr2 = new int[]{8, 7, 0, 0};
        System.out.println("Sum: " + Arrays.toString(np.sum(nr1, nr2)));

        int[] nr3 = new int[]{8, 3, 0, 1};
        int[] nr4 = new int[]{8, 3, 0, 0};
        System.out.println("Sub: " + Arrays.toString(np.sub(nr3, nr4)));

        int[] nr5 = new int[]{2, 3, 6, 0};
        System.out.println("Multiply : " + Arrays.toString(np.multiply(nr5, 2)));

        int[] nr6 = new int[]{8, 1};
        System.out.println("Divide: " + Arrays.toString(np.divide(nr6, 9)));

//        int number=np.buildNumber(nr1);
//        System.out.println(number);
    }
}
