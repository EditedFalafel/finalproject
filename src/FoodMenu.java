import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FoodMenu {
    ArrayList<MenuItem> menu = new ArrayList<>();
    ArrayList<String[]> addOns = new ArrayList<>();   // same order as menu
    ArrayList<String> categoryNames = new ArrayList<>();
    ArrayList<ArrayList<MenuItem>> categories = new ArrayList<>();



    public FoodMenu() {
        // burgers
        newCategory("Burgers");
        add("Classic Burger", 120.00, 8, new String[]{"Pickles", "Onions", "Cheese", "Tomatoes", "Lettuce", "Extra Patty", "Special Sauce"});
        add("Cheeseburger", 135.50, 8, new String[]{"Pickles", "Onions", "Cheese", "Tomatoes", "Lettuce", "Extra Patty", "Special Sauce"});
        add("Double Cheeseburger", 150.00, 10, new String[]{"Pickles", "Onions", "Cheese", "Tomatoes", "Lettuce", "Extra Patty", "Special Sauce"});
        add("Bacon Burger", 165.75, 10, new String[]{"Pickles", "Onions", "Cheese", "Tomatoes", "Lettuce", "Extra Patty", "Special Sauce"});
        add("Chicken Burger", 112.00, 9, new String[]{"Pickles", "Onions", "Cheese", "Tomatoes", "Lettuce", "Extra Patty", "Special Sauce"});


        // chicken
        newCategory("Chicken");
        add("Fried Chicken Meal", 99.00, 15, new String[]{"Gravy", "Rice"});
        add("Spicy Chicken Meal", 105.00, 15, new String[]{"Gravy", "Rice"});
        add("Chicken Tenders (4 pcs.)", 95.25, 8, new String[]{"Dip Sauce"});
        add("Chicken Nuggets (5 pcs.)", 110.00, 7, new String[]{"Dip Sauce"});

        // sides
        newCategory("Sides");
        add("Regular Fries", 60.00, 4, new String[]{"Cheese Flavor", "BBQ Flavor", "Sour Cream Flavor"});
        add("Large Fries", 75.00, 5, new String[]{"Cheese Flavor", "BBQ Flavor", "Sour Cream Flavor"});
        add("Mac n Cheese", 90.00, 6, new String[]{"Extra Cheese"});
        add("Mozzarella Sticks", 115.50, 6, new String[]{"Dip Sauce", "Cheese Flavor", "BBQ Flavor", "Sour Cream Flavor"});
        add("Onion Rings", 70.00, 5, new String[]{"Dip Sauce"});

        // drinks
        newCategory("Drinks");
        add("Coke", 40.00, 1, new String[]{"Ice"});
        add("Sprite", 40.00, 1, new String[]{"Ice"});
        add("Iced Tea", 35.00, 1, new String[]{"Ice", "Sugar"});
        add("Hot Coffee", 40.00, 3, new String[]{"Sugar", "Creamer", "Milk", "Espresso Shot"});
        add("Iced Coffee", 45.00, 2, new String[]{"Ice", "Sugar", "Creamer", "Milk", "Espresso Shot"});
        add("Chocolate Shake", 55.25, 3, new String[]{"Whipped Cream"});

        // desserts
        newCategory("Desserts");
        add("Sundae", 72.55, 2, new String[]{"Chocolate Syrup"});
        add("Apple Pie", 65.50, 4, new String[]{});
    }


    // adds item and its add-ons
    private void add(String name, double price, int cookTime, String[] extras) {
        MenuItem item = new MenuItem(name, price, cookTime, extras);
        menu.add(item);
        addOns.add(extras);
        categories.get(categories.size() - 1).add(item);
    }

    private void newCategory(String name) {
        categoryNames.add(name);
        categories.add(new ArrayList<>());
    }

    public ArrayList<MenuItem> getMenu() {
        return menu;
    }

    public String[] getCategoryNames() {
        return categoryNames.toArray(new String[0]);
    }

    // category number starts at 0
    public List<MenuItem> getItems(int categoryIndex) {
        return categories.get(categoryIndex);
    }

    // customer picks add-ons for an item, 0 means done or no add-ons
    public static String chooseAddOns(MenuItem item) {
        String[] extras = item.getIngredients();
        ArrayList<String> chosen = new ArrayList<>();

        if (extras == null || extras.length == 0) {
            return "";
        }

        while (true) {
            ScreenDisplay.clear();
            ScreenDisplay.boxTop("ADD-ONS");
            ScreenDisplay.boxCentered(item.getName());
            ScreenDisplay.boxSeparator();
            for (int i = 0; i < extras.length; i++) {
                String mark = "[ ]";
                if (chosen.contains(extras[i])) {
                    mark = "[x]";
                }
                ScreenDisplay.boxText("[" + (i + 1) + "] " + mark + " " + extras[i]);
            }
            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxText("[0] Done / No add-ons");
            ScreenDisplay.boxBottom();
            ScreenDisplay.print("(select an add-on again to remove it)");

            int choice = ScreenDisplay.readInt("Select:", 0, extras.length);

            if (choice == 0) {
                break;
            } else if (choice > 0 && choice <= extras.length) {
                String picked = extras[choice - 1];

                // picking it again removes it
                if (chosen.contains(picked)) {
                    chosen.remove(picked);
                } else {
                    chosen.add(picked);
                }
            }
        }

        return String.join(", ", chosen);
    }

    // number starts at 1
    public MenuItem getItem(int number) {
        if (number < 1 || number > menu.size()) {
            return null;
        }
        return menu.get(number - 1);
    }

    public String[] getAddOns(int number) {
        if (number < 1 || number > menu.size()) {
            return new String[0];
        }
        return addOns.get(number - 1);
    }

    public int size() {
        return menu.size();
    }

    public void display() {
        System.out.println("===== JMM HOUSE MENU =====");

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            System.out.println((i + 1) + ". " + item.getName());
            System.out.printf("   Price: Php %.2f%n", item.getPrice());
            System.out.println("   Cook time: " + item.getCookTime() + " min");

            String[] extras = addOns.get(i);
            if (extras.length > 0) {
                System.out.print("   Add-ons: ");
                for (int j = 0; j < extras.length; j++) {
                    System.out.print(extras[j]);
                    if (j != extras.length - 1) {
                        System.out.print(", ");
                    }
                }

                System.out.println();
            }

            System.out.println();
        }

    }


}