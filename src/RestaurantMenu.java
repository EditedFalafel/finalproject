import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
                    // add-ons are written as Name:price, a name with no price is free
                    List<AddOn> addOnList = new ArrayList<AddOn>();
                    for (String part : details[3].replace("{", "").replace("}", "").trim().split(",")) {
                        if (!part.trim().isEmpty()) {
                            addOnList.add(AddOn.parse(part));
                        }
                    }
                    AddOn[] ingredients = addOnList.toArray(new AddOn[0]);
                    menu.add(new MenuItem(name, price, time, ingredients));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading menu: " + e.getMessage());
        }

        return menu;
    }

    // customer ordering screens
    public void Menu(CustomerOrder currentOrder, FoodMenu foodMenu) {
        String[] categories = foodMenu.getCategoryNames();
        String message = "";

        while (true) {
            ScreenDisplay.clear();
            ScreenDisplay.boxTop("MENU");
            for (int i = 0; i < categories.length; i++) {
                ScreenDisplay.boxText("[" + (i + 1) + "] " + categories[i]);
            }
            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxText("[0] Review order / Checkout");
            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxRow("Items in order: " + currentOrder.getItemCount(), ScreenDisplay.money(currentOrder.getTotalPrice()));
            ScreenDisplay.boxRow(message, "Est. " + ScreenDisplay.time(currentOrder.getTotalCookTime()));
            ScreenDisplay.boxBottom();

            int categoryChoice = ScreenDisplay.readInt("Select a category:", 0, categories.length);

            if (categoryChoice == 0) {
                if (reviewOrder(currentOrder)) {
                    return;
                }
                message = "";
                continue;
            }

            message = chooseItem(foodMenu, categoryChoice - 1, currentOrder);
        }
    }

    // shows items of category
    private String chooseItem(FoodMenu foodMenu, int categoryIndex, CustomerOrder currentOrder) {
        String[] categories = foodMenu.getCategoryNames();
        List<MenuItem> items = foodMenu.getItems(categoryIndex);

        ScreenDisplay.clear();
        ScreenDisplay.boxTop(categories[categoryIndex].toUpperCase());
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            ScreenDisplay.boxRow("[" + (i + 1) + "] " + item.getName(), ScreenDisplay.time(item.getCookTime()) + "  " + ScreenDisplay.money(item.getPrice()));
        }
        ScreenDisplay.boxSeparator();
        ScreenDisplay.boxText("[0] Back");
        ScreenDisplay.boxBottom();

        int foodChoice = ScreenDisplay.readInt("Select an item:", 0, items.size());
        if (foodChoice == 0) {
            return "";
        }

        MenuItem selectedItem = items.get(foodChoice - 1);
        ArrayList<AddOn> addOns = FoodMenu.chooseAddOns(selectedItem);

        // price of ONE item with its add-ons
        double unitPrice = selectedItem.getPrice();
        for (AddOn a : addOns) {
            unitPrice += a.getPrice();
        }
        int quantity = chooseQuantity(selectedItem, unitPrice);

        if (quantity == 0) {
            return "";
        }

        for (int i = 0; i < quantity; i++) {
            currentOrder.addFoodItem(selectedItem, addOns);
        }

        return "Added " + quantity + "x " + selectedItem.getName();
    }

    // Quantity
    private int chooseQuantity(MenuItem item, double unitPrice) {
        ScreenDisplay.clear();
        ScreenDisplay.boxTop("QUANTITY");
        ScreenDisplay.boxCentered(item.getName());
        ScreenDisplay.boxRow("Unit Price (with add-ons):", ScreenDisplay.money(unitPrice));
        ScreenDisplay.boxSeparator();
        ScreenDisplay.boxText("[0] Cancel item"); //NAMTAN RAGEBAITER
        ScreenDisplay.boxBottom();

        return ScreenDisplay.readInt("Enter quantity:", 0, 99); //Napipikon ako
    }

    private boolean reviewOrder(CustomerOrder order) {
        while (true) {
            ScreenDisplay.clear();
            ScreenDisplay.boxTop("YOUR ORDER");
            if (order.isEmpty()) {
                ScreenDisplay.boxCentered("(no items yet)");
            }

            int n = 1;
            FoodItemNode current = order.getHead();
            while (current != null) {
                ScreenDisplay.boxRow(n + ". " + current.itemName, ScreenDisplay.money(current.price));
                if (!current.addOns.isEmpty()) {
                    ScreenDisplay.boxWrapped("     + ", current.addOns);
                }
                current = current.next;
                n++;
            }

            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxRow("SUBTOTAL", ScreenDisplay.money(order.getTotalPrice()));
            ScreenDisplay.boxRow("Estimated cooking time", ScreenDisplay.time(order.getTotalCookTime()));
            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxText("[1] Confirm order");
            ScreenDisplay.boxText("[2] Add more items");
            ScreenDisplay.boxText("[3] Remove an item");
            ScreenDisplay.boxText("[4] Cancel order");
            ScreenDisplay.boxBottom();

            int choice = ScreenDisplay.readInt("Select:", 1, 4);
            if (choice == 1) {
                if (order.isEmpty()) {
                    ScreenDisplay.print("Your order is empty. Please add an item first.");
                    ScreenDisplay.pause();
                    continue;
                }
                askDiscount(order);
                order.assignTicketNumber();
                IngredientInventory.deductOrder(order);
                showReceipt(order);
                new SortOrders().AddOrder(order);
                return true;
            } else if (choice == 2) {
                return false;
            } else if (choice == 3) {
                if (order.isEmpty()) {
                    continue;
                }
                int remove = ScreenDisplay.readInt("Item number to remove (0 = none):", 0, order.getItemCount());
                if (remove > 0) {
                    order.removeItem(remove);
                }
            } else {
                return true;
            }
        }
    }

    // asks if senior or PWD
    private void askDiscount(CustomerOrder order) {
        ScreenDisplay.clear();
        ScreenDisplay.boxTop("DISCOUNT");
        ScreenDisplay.boxCentered("Senior Citizen or PWD?");
        ScreenDisplay.boxCentered("(20% discount, no VAT)");
        ScreenDisplay.boxSeparator();
        ScreenDisplay.boxText("[1] Yes");
        ScreenDisplay.boxText("[2] No");
        ScreenDisplay.boxBottom();

        int choice = ScreenDisplay.readInt("Select:", 1, 2);
        if (choice == 1) {
            order.setSeniorOrPwd(true);
        }
    }

    // kiosk-style official receipt
    private void showReceipt(CustomerOrder order) {
        // group identical items (same name + same add-ons) into one line
        Map<String, FoodItemNode> firstOfKind = new LinkedHashMap<>();
        Map<String, Integer> counts = new LinkedHashMap<>();
        FoodItemNode node = order.getHead();
        while (node != null) {
            String key = node.itemName + "|" + node.addOns;
            firstOfKind.putIfAbsent(key, node);
            counts.merge(key, 1, Integer::sum);
            node = node.next;
        }

        // Philippine time, regardless of the computer's timezone setting
        String dateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"))
                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy  hh:mm a"));

        ScreenDisplay.clear();
        ScreenDisplay.boxTop("OFFICIAL RECEIPT");

        // header
        ScreenDisplay.boxGap();
        ScreenDisplay.boxCentered("J M M   H O U S E");
        ScreenDisplay.boxCentered("Taft Avenue, Malate, Manila");
        ScreenDisplay.boxCentered("VAT REG TIN: 000-000-000-000");
        ScreenDisplay.boxCentered("Tel: (02) 8000-0000");
        ScreenDisplay.boxGap();
        ScreenDisplay.boxSeparator();

        // order info
        ScreenDisplay.boxRow("Order No:", String.format("#%03d", order.customerId));
        ScreenDisplay.boxRow("Date:", dateTime);
        ScreenDisplay.boxRow("Terminal:", "KIOSK 01");
        ScreenDisplay.boxSeparator();

        // items
        ScreenDisplay.boxRow("ITEM", "AMOUNT");
        ScreenDisplay.boxSeparator();
        int totalQty = 0;
        for (String key : counts.keySet()) {
            FoodItemNode item = firstOfKind.get(key);
            int qty = counts.get(key);
            totalQty += qty;

            ScreenDisplay.boxRow(qty + "x " + item.itemName, ScreenDisplay.money(item.price * qty));
            ScreenDisplay.boxText("   @ " + ScreenDisplay.money(item.price) + " each");
            if (!item.addOns.isEmpty()) {
                ScreenDisplay.boxWrapped("   + ", item.addOns);
            }
        }
        ScreenDisplay.boxSeparator();

        // totals
        ScreenDisplay.boxRow("Total Items:", String.valueOf(totalQty));
        ScreenDisplay.boxRow("Subtotal", ScreenDisplay.money(order.getTotalPrice()));
        if (order.isSeniorOrPwd()) {
            ScreenDisplay.boxRow("Discount (20%)", "-" + ScreenDisplay.money(order.getDiscount()));
            ScreenDisplay.boxRow("VAT", "Exempt");
        } else {
            ScreenDisplay.boxRow("VAT (12%)", ScreenDisplay.money(order.getVat()));
        }
        ScreenDisplay.boxSeparator();
        ScreenDisplay.boxRow("TOTAL DUE", ScreenDisplay.money(order.getFinalTotal()));
        ScreenDisplay.boxRow("Estimated waiting time", ScreenDisplay.time(order.getTotalCookTime()));
        ScreenDisplay.boxSeparator();

        // senior / PWD details
        if (order.isSeniorOrPwd()) {
            ScreenDisplay.boxText("SC/PWD Name : ______________________");
            ScreenDisplay.boxText("SC/PWD ID No: ______________________");
            ScreenDisplay.boxText("Signature   : ______________________");
            ScreenDisplay.boxSeparator();
        }

        // footer
        ScreenDisplay.boxGap();
        ScreenDisplay.boxCentered("Please wait for your order number to be called.");
        ScreenDisplay.boxGap();
        ScreenDisplay.boxCentered("Your order number is");
        ScreenDisplay.boxCentered(String.format("#%03d", order.customerId));
        ScreenDisplay.boxGap();
        ScreenDisplay.boxCentered("THIS SERVES AS YOUR OFFICIAL RECEIPT");
        ScreenDisplay.boxGap();
        ScreenDisplay.boxCentered("Thank you for ordering at JMM House!");
        ScreenDisplay.boxGap();
        ScreenDisplay.boxBottom();
        ScreenDisplay.pause();
    }
}