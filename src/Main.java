public class Main {
    public void main() {
        FoodMenu foodMenu = new FoodMenu();
        IngredientInventory.initializeFromMenu(foodMenu);

        while (true) {
            ScreenDisplay.clear();
            ScreenDisplay.boxTop("J M M   H O U S E");
            ScreenDisplay.boxGap();
            ScreenDisplay.boxCentered("Welcome to the JMM House");
            ScreenDisplay.boxCentered("Ordering System");
            ScreenDisplay.boxGap();
            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxText("[1] Start a new order");
            ScreenDisplay.boxText("[2] Admin View");
            ScreenDisplay.boxText("[0] Exit");
            ScreenDisplay.boxBottom();

            int choice = ScreenDisplay.readInt("Select:", 0, 2);

            if (choice == 1) {
                new RestaurantMenu().Menu(new CustomerOrder(), foodMenu);
            } else if (choice == 2) {
                AdminView.menu(foodMenu);
            } else {
                SalesReport.display();
                ScreenDisplay.blank();
                ScreenDisplay.print("Thank you! See you again at JMM House.");
                ScreenDisplay.blank();
                return;
            }
        }
    }

    public static void main(String[] args) {
        new Main().main();
    }
}