import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

class RestaurantMenu {

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
        CustomerOrder currentOrder = new CustomerOrder();
        Menu(currentOrder);
    }

    public void Menu(CustomerOrder currentOrder) {
        //replace test menu with real menu pls
        FoodMenu foodMenu = new FoodMenu();
        String[] categories = foodMenu.getCategoryNames();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the JMM House Ordering System!");

        while (true) {
            int categoryChoice = -1;
            do {
                System.out.println("\n--- Menu ---");
                for (int i = 0; i < categories.length; i++) {
                    System.out.println("[" + (i + 1) + "] " + categories[i]);
                }
                System.out.println("[0] Finish");

                System.out.print("Select a category: ");
                categoryChoice = scanner.nextInt();
            } while (categoryChoice > categories.length);

            if (categoryChoice == 0) {
                break;
            }

            List<MenuItem> menu = foodMenu.getItems(categoryChoice - 1);

            System.out.println();

            int foodChoice;
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


                String addOns = FoodMenu.chooseAddOns(scanner, selectedItem);
                currentOrder.addFoodItem(selectedItem, addOns);
                System.out.println(selectedItem.getName() + " added to your order.");
                System.out.printf("Total cooking time so far: %.2f mins\n", currentOrder.getTotalCookTime());
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }

        currentOrder.displayOrderSummary();
        while(true) {
            System.out.print("Press 1 to confirm order, 0 to retry: ");
            int confirm = scanner.nextInt();

            if (confirm == 1) {
                break;
            } else if (confirm == 0){
                Menu(currentOrder);
                break;
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        RestaurantMenu m = new RestaurantMenu();
        m.Main();
    }
}