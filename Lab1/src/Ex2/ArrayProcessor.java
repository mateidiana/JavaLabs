package Ex2;

public class ArrayProcessor {
    public int maxElem(final int[] array) {
        validateArray(array);
        int max = Integer.MIN_VALUE;
        for (final int elem : array) {
            if (elem > max) {
                max = elem;
            }
        }
        return max;
    }

    public int minElem(final int[] array) {
        validateArray(array);
        int min = Integer.MAX_VALUE;
        for (final int elem : array) {
            if (elem < min) {
                min = elem;
            }
        }
        return min;
    }

    public int maxSum(final int[] array) {
        validateArray(array);
        int sum = arraySum(array);
        int min = minElem(array);
        return sum - min;
    }

    public int minSum(final int[] array) {
        validateArray(array);
        int sum = arraySum(array);
        int max = maxElem(array);
        return sum - max;
    }

    public int arraySum(final int[] array) {
        validateArray(array);
        int sum = 0;
        for (final int elem : array) {
            sum += elem;
        }
        return sum;
    }

    private void validateArray(final int[] array) {
        if (array.length == 0) {
            throw new RuntimeException("Array can't be empty");
        }
    }
}
