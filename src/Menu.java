import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Menu {

    // DO NOT TOUCH THIS IS HELD TOGETHER WITH DREAMS AND THE SONGS HAPPY FAMILY AND LUNAR IS HERE
    public List<MenuItem> loadMenuFromFile(String filePath) {
        List<MenuItem> menu = new ArrayList<MenuItem>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(";");
                if (details.length >= 3) {
                    String name = details[0].trim();
                    double price = Double.parseDouble(details[1].replaceAll("[^0-9.]", "").trim());
                    double time = Double.parseDouble(details[2].replaceAll("[^0-9.]", "").trim());
                    String[] ingredients = details[3].replace("{", "").replace("}", "").trim().split(",");
                    menu.add(new MenuItem(name, price, time, ingredients));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading menu: " + e.getMessage());
        }

        return menu;
    }

    public void Main() {
        //replace test menu with real menu pls
        File folder = new File("src/MenuCategories");

        File[] categories = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        Scanner scanner = new Scanner(System.in);
        CustomerOrder currentOrder = new CustomerOrder();

        System.out.println("Welcome to the JMM House Ordering System!");

        boolean ordering = true;
        while (ordering) {
            boolean incategory = true;

            int categoryChoice = -1;
            do {
                System.out.println("\n--- Menu ---");
                for (int i = 0; i < categories.length; i++) {
                    System.out.println("[" + (i + 1) + "] " + categories[i].getName().replace(".txt", ""));
                }
                System.out.println("[0] Exit");

                System.out.print("Select a category: ");
                categoryChoice = scanner.nextInt();
            } while (categoryChoice > categories.length);

            if (categoryChoice == 0) {
                break;
            }

            List<MenuItem> menu = loadMenuFromFile(categories[categoryChoice-1].toString());

            System.out.println();

            int foodChoice = -1;
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.get(i);
                System.out.printf("[%d] %s - %.2f mins - Php%.2f\n", i + 1, item.getName(), item.getCookTime(), item.getPrice());
            }
            System.out.println("[0] Back");

            System.out.print("Select an item to add to your order: ");

            foodChoice = scanner.nextInt();

            if (foodChoice == 0) {
                continue;
            } else if (foodChoice > 0 && foodChoice <= menu.size()) {
                MenuItem selectedItem = menu.get(foodChoice - 1);


                currentOrder.addFoodItem(selectedItem);
                System.out.println(selectedItem.getName() + " added to your order.");
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }

        currentOrder.displayOrderSummary();
        scanner.close();
    }

    public static void main(String[] args) {
        Menu m = new Menu();
        m.Main();
    }
}
