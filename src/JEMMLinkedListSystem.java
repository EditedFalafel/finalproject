import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MenuItem {
    String name;
    int cookTime;
    double price;

    public MenuItem(String name, int cookTime, double price) {
        this.name = name;
        this.cookTime = cookTime;
        this.price = price;
    }
}

class FoodItemNode {
    String itemName;
    int cookTime;
    double price;
    FoodItemNode next;

    public FoodItemNode(String itemName, int cookTime, double price) {
        this.itemName = itemName;
        this.cookTime = cookTime;
        this.price = price;
        this.next = null;
    }
}

class CustomerOrder {
    private FoodItemNode head;
    public int customerId;
    private static int customerCounter = 1;

    public CustomerOrder() {
        this.customerId = customerCounter++;
    }

    // Adds order
    public void addFoodItem(MenuItem item) {
        FoodItemNode newNode = new FoodItemNode(item.name, item.cookTime, item.price);
        if (head == null) {
            head = newNode;
        } else {
            FoodItemNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    // math for serving time
    public int getTotalCookTime() {
        int total = 0;
        FoodItemNode current = head;
        while (current != null) {
            total += current.cookTime;
            current = current.next;
        }
        return total;
    }

    // math for price
    public double getTotalPrice() {
        double total = 0;
        FoodItemNode current = head;
        while (current != null) {
            total += current.price;
            current = current.next;
        }
        return total;
    }

    public void displayOrderSummary() {
        System.out.println("\n--- Order Summary (Ticket #" + customerId + ") ---");
        FoodItemNode current = head;
        if (current == null) {
            System.out.println("No items ordered.");
            return;
        }
        while (current != null) {
            System.out.printf("- %s (Cook time: %d mins, $%.2f)\n", current.itemName, current.cookTime, current.price);
            current = current.next;
        }
        System.out.println("-----------------------------------");
        System.out.printf("Total Amount: $%.2f\n", getTotalPrice());
        System.out.println("Estimated Total Waiting Time: " + getTotalCookTime() + " mins");
    }
}

public class JEMMLinkedListSystem {
    private static List<MenuItem> menu = new ArrayList<>();

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

                    menu.add(new MenuItem(name, time, price));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading menu: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Initializes the file reading process
        loadMenuFromFile("C:\\Users\\User\\IdeaProjects\\untitled29\\src\\Menu Sample.txt"); //replace test menu with real menu pls
        Scanner scanner = new Scanner(System.in);

        CustomerOrder currentOrder = new CustomerOrder();

        System.out.println("Welcome to the JMM House Ordering System!");

        boolean ordering = true;
        while (ordering) {
            System.out.println("\n--- Menu ---");
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.get(i);
                System.out.printf("[%d] %s - %d mins - $%.2f\n", i + 1, item.name, item.cookTime, item.price);
            }
            System.out.println("[0] Finish Order");
            System.out.print("Select an item to add to your order: ");

            int choice = scanner.nextInt();

            if (choice == 0) {
                ordering = false;
            } else if (choice > 0 && choice <= menu.size()) {
                MenuItem selectedItem = menu.get(choice - 1);


                currentOrder.addFoodItem(selectedItem);
                System.out.println(selectedItem.name + " added to your order.");
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }

        currentOrder.displayOrderSummary();
        scanner.close();
    }
}