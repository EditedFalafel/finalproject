import java.util.ArrayList;

public class FoodMenu {
    ArrayList<MenuItem> menu = new ArrayList<>();
    ArrayList<String[]> addOns = new ArrayList<>();   // same order as menu



    public FoodMenu() {
        // burgers
        add("Classic Burger", 120.00, 8, new String[]{"Pickles", "Onions", "Cheese", "Tomatoes", "Lettuce", "Extra Patty", "Special Sauce"});
        add("Cheeseburger", 135.50, 8, new String[]{"Pickles", "Onions", "Cheese", "Tomatoes", "Lettuce", "Extra Patty", "Special Sauce"});
        add("Double Cheeseburger", 150.00, 10, new String[]{"Pickles", "Onions", "Cheese", "Tomatoes", "Lettuce", "Extra Patty", "Special Sauce"});
        add("Bacon Burger", 165.75, 10, new String[]{"Pickles", "Onions", "Cheese", "Tomatoes", "Lettuce", "Extra Patty", "Special Sauce"});
        add("Chicken Burger", 112.00, 9, new String[]{"Pickles", "Onions", "Cheese", "Tomatoes", "Lettuce", "Extra Patty", "Special Sauce"});


        // chicken
        add("Fried Chicken Meal", 99.00, 15, new String[]{"Gravy", "Rice"});
        add("Spicy Chicken Meal", 105.00, 15, new String[]{"Gravy", "Rice"});
        add("Chicken Tenders (4 pcs.)", 95.25, 8, new String[]{"Dip Sauce"});
        add("Chicken Nuggets (5 pcs.)", 110.00, 7, new String[]{"Dip Sauce"});

        // sides
        add("Regular Fries", 60.00, 4, new String[]{"Cheese Flavor", "BBQ Flavor", "Sour Cream Flavor"});
        add("Large Fries", 75.00, 5, new String[]{"Cheese Flavor", "BBQ Flavor", "Sour Cream Flavor"});
        add("Mac n Cheese", 90.00, 6, new String[]{"Extra Cheese"});
        add("Mozzarella Sticks", 115.50, 6, new String[]{"Dip Sauce", "Cheese Flavor", "BBQ Flavor", "Sour Cream Flavor"});
        add("Onion Rings", 70.00, 5, new String[]{"Dip Sauce"});

        // drinks
        add("Coke", 40.00, 1, new String[]{"Ice"});
        add("Sprite", 40.00, 1, new String[]{"Ice"});
        add("Iced Tea", 35.00, 1, new String[]{"Ice", "Sugar"});
        add("Hot Coffee", 40.00, 3, new String[]{"Sugar", "Creamer", "Milk", "Espresso Shot"});
        add("Iced Coffee", 45.00, 2, new String[]{"Ice", "Sugar", "Creamer", "Milk", "Espresso Shot"});
        add("Chocolate Shake", 55.25, 3, new String[]{"Whipped Cream"});

        // desserts
        add("Sundae", 72.55, 2, new String[]{"Chocolate Syrup"});
        add("Apple Pie", 65.50, 4, new String[]{});
    }


    // adds item and its add-ons
    private void add(String name, double price, int cookTime, String[] extras) {
        menu.add(new MenuItem(name, cookTime, price));
        addOns.add(extras);
    }

    public ArrayList<MenuItem> getMenu() {
        return menu;
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
            System.out.println((i + 1) + ". " + item.name);
            System.out.printf("   Price: Php %.2f%n", item.price);
            System.out.println("   Cook time: " + item.cookTime + " min");

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