package Ex3;

public class NumberProcessor {
    public int[] sum(final int[] nr1, final int[] nr2) {
        validateNumber(nr1);
        validateNumber(nr2);
        int[] result = new int[nr1.length];
        int carry = 0;
        for (int i = nr1.length - 1; i >= 0; i--) {
            result[i] = (nr1[i] + nr2[i] + carry) % 10;
            carry = (nr1[i] + nr2[i] + carry) / 10;
        }
        if (carry > 0) {
            result = extend(result, carry);
        }
        return result;
    }

    public int[] extend(final int[] number, final int carry) {
        int[] result = new int[number.length + 1];
        result[0] = carry;
        System.arraycopy(number, 0, result, 1, number.length);
        validateNumber(result);
        return result;
    }

    public int[] sub(final int[] nr1, final int[] nr2) {
        validateNumber(nr1);
        validateNumber(nr2);

        if (buildNumber(nr1)<buildNumber(nr2))
            throw new RuntimeException("Subtraction will be negative");

        int[] result = new int[nr1.length];
        int carry = 0;
        for (int i = nr1.length - 1; i >= 0; i--) {
            if (nr1[i] >= nr2[i] + carry) {
                result[i] = nr1[i] - nr2[i] - carry;
                carry = 0;
            } else {
                result[i] = 10 + nr1[i] - nr2[i] - carry;
                carry = 1;
            }
        }
        if (result[0] == 0) {
            result = makeSmaller(result);
        }
        return result;
    }

    public int[] makeSmaller(final int[] number) {
        int j = 0;
        while (number[j] == 0) {
            j++;
        }
        int[] result = new int[number.length - j];
        System.arraycopy(number, j, result, 0, result.length);
        validateNumber(result);
        return result;
    }

    public int[] multiply(final int[] number, final int multiplier) {
        validateNumber(number);
        validateDigit(multiplier);
        int[] result = new int[number.length];
        int carry = 0;
        for (int i = number.length - 1; i >= 0; i--) {
            int product = number[i] * multiplier + carry;
            result[i] = product % 10;
            carry = product / 10;
        }
        if (carry > 0) {
            result = extend(result, carry);
        }
        return result;
    }

    public int[] divide(final int[] number, final int divisor) {
        validateNumber(number);
        validateDigit(divisor);
        if (divisor == 0) {
            throw new RuntimeException("Can't divide by 0.");
        }

        int[] result = new int[number.length];
        int remainder = 0;
        for (int i = 0; i < number.length; i++) {
            int currentDigit = number[i] + remainder * 10;
            result[i] = currentDigit / divisor;
            remainder = currentDigit % divisor;
        }
        if (result[0] == 0) {
            result = makeSmaller(result);
        }
        return result;
    }

    private void validateNumber(int[] number) {
        if (number.length == 0) {
            throw new RuntimeException("Number should not be null.");
        }
        if (number[0] == 0 && number.length > 1) {
            throw new RuntimeException("Number can't start with 0.");
        }
        for (int digit : number) {
            validateDigit(digit);
        }
    }

    private void validateDigit(final int digit) {
        if (digit > 9 || digit < 0) {
            throw new RuntimeException("Invalid digit.");
        }
    }

    public int buildNumber(final int[] number) {
        int result=0;
        int multiplier=1;
        for (int i=number.length-1;i>=0;i--)
        {
            result=result+number[i]*multiplier;
            multiplier=multiplier*10;
        }
        return result;
    }
}
