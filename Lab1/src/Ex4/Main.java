package Ex4;

public class Main {
    public static void main(String[] args) {
        ElectronicsShop es = new ElectronicsShop();
        int[] keyboards = new int[]{40, 50, 60};
        int[] usb = new int[]{8, 12};

        System.out.println("Cheapest keyboard: " + es.cheapestItem(keyboards));

        System.out.println("Most expensive keyboard: " + es.mostExpensiveItem(keyboards));
        System.out.println("Most expensive usb: " + es.mostExpensiveItem(usb));

        System.out.println("Most expensive and affordable usb: " + es.mostExpensiveAndAffordableItem(usb, 10));

        System.out.println("Best combination of items: " + es.bestCombinationPrice(keyboards, usb, 60));
    }
}
