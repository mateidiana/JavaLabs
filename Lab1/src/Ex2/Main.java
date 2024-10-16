package Ex2;

public class Main {
    public static void main(String[] args) {
        ArrayProcessor ap = new ArrayProcessor();
        int[] example = new int[]{-4, 8, 3, 10, 17};

        System.out.println("Min sum: " + ap.minSum(example));
        System.out.println("Max sum: " + ap.maxSum(example));
        System.out.println("Min elem: " + ap.minElem(example));
        System.out.println("Max elem: " + ap.maxElem(example));
    }
}
