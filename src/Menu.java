import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static List<MenuItem> menu = new FoodMenu().getMenu();

    // DO NOT TOUCH THIS IS HELD TOGETHER WITH DREAMS AND THE SONGS HAPPY FAMILY AND LUNAR IS HERE
    public static void loadMenuFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 3) {
                    String name = details[0].trim();
                    int time = Integer.parseInt(details[1].replaceAll("[^0-9]", "").trim());
                    double price = Double.parseDouble(details[2].replaceAll("[^0-9.]", "").trim());

                    menu.add(new MenuItem(name, price, time));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading menu: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        //replace test menu with real menu pls
        Scanner scanner = new Scanner(System.in);

        CustomerOrder currentOrder = new CustomerOrder();

        System.out.println("Welcome to the JMM House Ordering System!");

        boolean ordering = true;
        while (ordering) {
            System.out.println("\n--- Menu ---");
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.get(i);
                System.out.printf("[%d] %s - %.2f mins - Php%.2f\n", i + 1, item.getName(), item.getCookTime(), item.getPrice());
            }
            System.out.println("[0] Finish Order");
            System.out.print("Select an item to add to your order: ");

            int choice = scanner.nextInt();

            if (choice == 0) {
                ordering = false;
            } else if (choice > 0 && choice <= menu.size()) {
                MenuItem selectedItem = menu.get(choice - 1);


                currentOrder.addFoodItem(selectedItem);
                System.out.println(selectedItem.getName() + " added to your order.");
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }

        currentOrder.displayOrderSummary();
        scanner.close();
    }
}
