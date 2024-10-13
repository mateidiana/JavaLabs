package Ex4;

public class ElectronicsShop {
    public int cheapestItem(int[] itemArray) {
        validateArray(itemArray);
        int min = Integer.MAX_VALUE;
        for (int item : itemArray) {
            if (min > item) {
                min = item;
            }
        }
        return min;
    }

    public int mostExpensiveItem(int[] itemArray) {
        validateArray(itemArray);
        int max = Integer.MIN_VALUE;
        for (int item : itemArray) {
            if (max < item) {
                max = item;
            }
        }
        return max;
    }

    public int mostExpensiveAndAffordableItem(int[] itemArray, int budget) {
        validateArray(itemArray);
        validateBudget(budget);
        int max = Integer.MIN_VALUE;
        for (int item : itemArray) {
            if (max < item && item < budget) {
                max = item;
            }
        }
        return max;
    }

    public int bestCombinationPrice(int[] itemArray1, int[] itemArray2, int budget) {
        validateArray(itemArray1);
        validateArray(itemArray2);
        validateBudget(budget);
        int maxSum = 0;
        for (int i = 0; i < itemArray1.length; i++) {
            for (int j = 0; j < itemArray2.length; j++) {
                int currentSum = itemArray1[i] + itemArray2[j];
                if (currentSum <= budget && currentSum > maxSum) {
                    maxSum = currentSum;
                }
            }
        }
        if (maxSum == 0) {
            return -1;
        }
        return maxSum;
    }

    private void validateArray(int[] array) {
        if (array.length == 0) {
            throw new RuntimeException("Array should not be empty.");
        }
        for (int price : array) {
            if (price < 0) {
                throw new RuntimeException("Prices can't be negative.");
            }
        }
    }

    private void validateBudget(int budget) {
        if (budget < 0) {
            throw new RuntimeException("Budget should not be negative.");
        }
    }
}
