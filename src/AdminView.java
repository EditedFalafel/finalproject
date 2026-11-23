import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminView {

    public static void menu(FoodMenu foodMenu) {
        while (true) {
            ScreenDisplay.clear();
            ScreenDisplay.boxTop("ADMIN DASHBOARD");
            ScreenDisplay.boxText("[1] Add Menu Item");
            ScreenDisplay.boxText("[2] Delete Menu Item");
            ScreenDisplay.boxText("[3] Check Ingredients Stock");
            ScreenDisplay.boxText("[4] Manage & Expedite Orders");
            ScreenDisplay.boxText("[5] View Sales Report");
            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxText("[0] Back to Main Menu");
            ScreenDisplay.boxBottom();

            int choice = ScreenDisplay.readInt("Select an option:", 0, 5);

            switch (choice) {
                case 1:
                    addMenuItem(foodMenu);
                    break;
                case 2:
                    deleteMenuItem(foodMenu);
                    break;
                case 3:
                    checkIngredientsStock();
                    break;
                case 4:
                    manageOrders();
                    break;
                case 5:
                    SalesReport.display();
                    break;
                case 0:
                    return;
            }
        }
    }

    // 1. ADD MENU ITEM
    private static void addMenuItem(FoodMenu foodMenu) {
        String[] categories = foodMenu.getCategoryNames();
        if (categories == null || categories.length == 0) {
            ScreenDisplay.print("No menu categories available.");
            ScreenDisplay.pause();
            return;
        }

        ScreenDisplay.clear();
        ScreenDisplay.boxTop("ADD MENU ITEM");
        for (int i = 0; i < categories.length; i++) {
            ScreenDisplay.boxText("[" + (i + 1) + "] " + categories[i]);
        }
        ScreenDisplay.boxSeparator();
        ScreenDisplay.boxText("[0] Cancel");
        ScreenDisplay.boxBottom();

        int catChoice = ScreenDisplay.readInt("Select category:", 0, categories.length);
        if (catChoice == 0) return;

        int catIndex = catChoice - 1;
        String name = ScreenDisplay.readString("Enter item name:");
        double price = ScreenDisplay.readDouble("Enter price (Php):", 1.0, 10000.0);
        double cookTime = ScreenDisplay.readDouble("Enter cook time (mins):", 0.5, 180.0);
        String rawIngredients = ScreenDisplay.readOptionalString("Add-ons (Name:price, comma-separated, or blank):");

        AddOn[] ingredients;
        if (rawIngredients.isEmpty()) {
            ingredients = new AddOn[0];
        } else {
            List<AddOn> cleaned = new ArrayList<>();
            for (String part : rawIngredients.split(",")) {
                if (!part.trim().isEmpty()) {
                    cleaned.add(AddOn.parse(part));
                }
            }
            ingredients = cleaned.toArray(new AddOn[0]);
        }

        MenuItem newItem = new MenuItem(name, price, cookTime, ingredients);
        foodMenu.addItem(catIndex, newItem);
        saveCategoryToFile(categories[catIndex], foodMenu.getItems(catIndex));

        if (ingredients.length > 0) {
            int initStock = ScreenDisplay.readInt("Initial stock for new ingredients (0-500):", 0, 500);
            IngredientInventory.registerIngredients(ingredients, initStock);
        }

        ScreenDisplay.clear();
        ScreenDisplay.boxTop("ITEM ADDED");
        ScreenDisplay.boxRow("Category:", categories[catIndex]);
        ScreenDisplay.boxRow("Item:", newItem.getName());
        ScreenDisplay.boxRow("Price:", ScreenDisplay.money(newItem.getPrice()));
        ScreenDisplay.boxRow("Cook Time:", ScreenDisplay.time(newItem.getCookTime()));
        ScreenDisplay.boxBottom();
        ScreenDisplay.pause();
    }

    // 2. DELETE MENU ITEM
    private static void deleteMenuItem(FoodMenu foodMenu) {
        String[] categories = foodMenu.getCategoryNames();
        if (categories == null || categories.length == 0) {
            ScreenDisplay.print("No menu categories available.");
            ScreenDisplay.pause();
            return;
        }

        ScreenDisplay.clear();
        ScreenDisplay.boxTop("DELETE MENU ITEM");
        for (int i = 0; i < categories.length; i++) {
            ScreenDisplay.boxText("[" + (i + 1) + "] " + categories[i]);
        }
        ScreenDisplay.boxSeparator();
        ScreenDisplay.boxText("[0] Cancel");
        ScreenDisplay.boxBottom();

        int catChoice = ScreenDisplay.readInt("Select category:", 0, categories.length);
        if (catChoice == 0) return;

        int catIndex = catChoice - 1;
        List<MenuItem> items = foodMenu.getItems(catIndex);
        if (items == null || items.isEmpty()) {
            ScreenDisplay.print("No items in " + categories[catIndex] + ".");
            ScreenDisplay.pause();
            return;
        }

        ScreenDisplay.clear();
        ScreenDisplay.boxTop("DELETE FROM " + categories[catIndex].toUpperCase());
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            ScreenDisplay.boxRow(
                    "[" + (i + 1) + "] " + item.getName(),
                    ScreenDisplay.time(item.getCookTime()) + "  " + ScreenDisplay.money(item.getPrice())
            );
        }
        ScreenDisplay.boxSeparator();
        ScreenDisplay.boxText("[0] Cancel");
        ScreenDisplay.boxBottom();

        int itemChoice = ScreenDisplay.readInt("Select item to delete:", 0, items.size());
        if (itemChoice == 0) return;

        MenuItem removed = items.get(itemChoice - 1);
        foodMenu.removeItem(catIndex, itemChoice - 1);
        saveCategoryToFile(categories[catIndex], foodMenu.getItems(catIndex));

        ScreenDisplay.clear();
        ScreenDisplay.boxTop("ITEM DELETED");
        ScreenDisplay.boxCentered("Removed: " + removed.getName());
        ScreenDisplay.boxBottom();
        ScreenDisplay.pause();
    }

    // Persists updated category items back to Burgers.txt, Chicken.txt, etc.
    private static void saveCategoryToFile(String categoryName, List<MenuItem> items) {
        if (categoryName == null || items == null) return;

        String fileName = categoryName.trim() + ".txt";
        File target = new File(FoodMenu.resolveFilePath(fileName));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(target, false))) {
            for (MenuItem item : items) {
                String cookStr = (item.getCookTime() == Math.floor(item.getCookTime()))
                        ? String.valueOf((int) item.getCookTime())
                        : String.format(Locale.US, "%.1f", item.getCookTime());

                List<String> validIngs = new ArrayList<>();
                if (item.getIngredients() != null) {
                    for (AddOn ing : item.getIngredients()) {
                        if (ing != null && !ing.getName().trim().isEmpty()) {
                            validIngs.add(ing.toFileString());
                        }
                    }
                }

                String line = String.format(
                        Locale.US,
                        "%s; %.2f; %s; {%s}",
                        item.getName(),
                        item.getPrice(),
                        cookStr,
                        String.join(", ", validIngs)
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            ScreenDisplay.print("Warning: Could not update " + fileName + ": " + e.getMessage());
        }
    }

    // 3. CHECK & RESTOCK INGREDIENTS STOCK
    private static void checkIngredientsStock() {
        while (true) {
            List<String> ingredientNames = IngredientInventory.getIngredientNames();
            int lowCount = 0;
            int outCount = 0;

            ScreenDisplay.clear();
            ScreenDisplay.boxTop("INGREDIENTS STOCK");

            if (ingredientNames.isEmpty()) {
                ScreenDisplay.boxCentered("(no ingredients tracked yet)");
            } else {
                for (int i = 0; i < ingredientNames.size(); i++) {
                    String ingName = ingredientNames.get(i);
                    int qty = IngredientInventory.getQuantity(ingName);
                    String status = IngredientInventory.getStatusLabel(qty);
                    if (qty <= 0) outCount++;
                    else if (qty <= IngredientInventory.LOW_STOCK_THRESHOLD) lowCount++;

                    ScreenDisplay.boxRow(
                            "[" + (i + 1) + "] " + ingName,
                            qty + " units  [" + status + "]"
                    );
                }
            }

            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxRow("Low Stock: " + lowCount, "Out of Stock: " + outCount);
            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxText("[1] Restock existing ingredient");
            ScreenDisplay.boxText("[2] Add new ingredient");
            ScreenDisplay.boxText("[0] Back");
            ScreenDisplay.boxBottom();

            int choice = ScreenDisplay.readInt("Select:", 0, 2);
            if (choice == 0) {
                return;
            } else if (choice == 1) {
                if (ingredientNames.isEmpty()) {
                    ScreenDisplay.print("No ingredients to restock.");
                    ScreenDisplay.pause();
                    continue;
                }
                int idx = ScreenDisplay.readInt("Ingredient number to restock:", 1, ingredientNames.size()) - 1;
                int addQty = ScreenDisplay.readInt("Quantity to add (1-500):", 1, 500);
                String targetName = ingredientNames.get(idx);
                IngredientInventory.addOrRestock(targetName, addQty);
                ScreenDisplay.print("Restocked " + targetName + " (+" + addQty + ").");
                ScreenDisplay.pause();
            } else {
                String newIng = ScreenDisplay.readString("Enter new ingredient name:");
                int initQty = ScreenDisplay.readInt("Initial quantity (0-500):", 0, 500);
                IngredientInventory.addOrRestock(newIng, initQty);
                ScreenDisplay.print("Added " + newIng + " (" + initQty + " units).");
                ScreenDisplay.pause();
            }
        }
    }

    // 4. EXPEDITE, COMPLETE, OR DELETE ORDERS
    private static void manageOrders() {
        SortOrders sortOrders = new SortOrders();

        while (true) {
            List<CustomerOrder> activeOrders = sortOrders.getActiveOrders();

            ScreenDisplay.clear();
            ScreenDisplay.boxTop("ORDER QUEUE MANAGEMENT");

            if (activeOrders.isEmpty()) {
                ScreenDisplay.boxCentered("(no active orders in queue)");
            } else {
                for (int i = 0; i < activeOrders.size(); i++) {
                    CustomerOrder order = activeOrders.get(i);
                    String tag = order.isExpedited() ? " [EXPEDITED]" : "";
                    ScreenDisplay.boxRow(
                            "#" + (i + 1) + "  Order #" + order.customerId + tag,
                            ScreenDisplay.time(order.getTotalCookTime()) + "  " + ScreenDisplay.money(order.getFinalTotal())
                    );

                    FoodItemNode node = order.getHead();
                    while (node != null) {
                        String line = "   - " + node.itemName;
                        if (node.addOns != null && !node.addOns.isEmpty()) {
                            line += " (+" + node.addOns + ")";
                        }
                        ScreenDisplay.boxText(line);
                        node = node.next;
                    }
                    if (i < activeOrders.size() - 1) {
                        ScreenDisplay.boxSeparator();
                    }
                }
            }

            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxText("[1] Expedite an order (move to front)");
            ScreenDisplay.boxText("[2] Complete / Serve next order");
            ScreenDisplay.boxText("[3] Delete / Cancel an order");
            ScreenDisplay.boxText("[0] Back");
            ScreenDisplay.boxBottom();

            int choice = ScreenDisplay.readInt("Select:", 0, 3);
            if (choice == 0) {
                return;
            } else if (choice == 1) {
                if (activeOrders.isEmpty()) {
                    ScreenDisplay.print("No orders available to expedite.");
                    ScreenDisplay.pause();
                    continue;
                }
                int ticketId = ScreenDisplay.readInt("Enter Order # to expedite:", 1, 99999);
                if (sortOrders.expediteOrder(ticketId)) {
                    ScreenDisplay.print("Order #" + ticketId + " expedited to the front of the queue!");
                } else {
                    ScreenDisplay.print("Order #" + ticketId + " was not found in active orders.");
                }
                ScreenDisplay.pause();
            } else if (choice == 2) {
                CustomerOrder served = sortOrders.completeNextOrder();
                if (served == null) {
                    ScreenDisplay.print("No orders in queue.");
                } else {
                    SalesReport.addSale(served);
                    ScreenDisplay.print("Completed & served Order #" + served.customerId + ".");
                }
                ScreenDisplay.pause();
            } else {
                if (activeOrders.isEmpty()) {
                    ScreenDisplay.print("No orders available to delete.");
                    ScreenDisplay.pause();
                    continue;
                }
                int ticketId = ScreenDisplay.readInt("Enter Order # to cancel/delete:", 1, 99999);
                if (sortOrders.deleteOrder(ticketId)) {
                    ScreenDisplay.print("Order #" + ticketId + " deleted from queue.");
                } else {
                    ScreenDisplay.print("Order #" + ticketId + " was not found in active orders.");
                }
                ScreenDisplay.pause();
            }
        }
    }
}