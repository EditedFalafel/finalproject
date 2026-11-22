import java.util.ArrayList;
import java.util.List;

public class IngredientInventory {
    private static final List<String> names = new ArrayList<>();
    private static final List<Integer> quantities = new ArrayList<>();
    private static final int DEFAULT_INITIAL_STOCK = 20;
    public static final int LOW_STOCK_THRESHOLD = 5;
//khun mae kha Lunar is here
//nong yai cha Lunar is here
//nong ta cha Lunar is here
//muan choi muan choi aya khoi aya khoi
//Everybody cha Lunar is here
    public static void initializeFromMenu(FoodMenu foodMenu) {
        String[] categories = foodMenu.getCategoryNames();
        if (categories == null) return;

        for (int i = 0; i < categories.length; i++) {
            List<MenuItem> items = foodMenu.getItems(i);
            if (items == null) continue;
            for (MenuItem item : items) {
                registerIngredients(item.getIngredients(), DEFAULT_INITIAL_STOCK);
            }
        }
    }

    public static void registerIngredients(String[] ingredients, int initialQty) {
        if (ingredients == null) return;
        for (String raw : ingredients) {
            String key = normalize(raw);
            if (!key.isEmpty() && indexOf(key) == -1) {
                names.add(key);
                quantities.add(Math.max(0, initialQty));
            }
        }
    }

    public static void addOrRestock(String ingredientName, int quantityToAdd) {
        String key = normalize(ingredientName);
        if (key.isEmpty() || quantityToAdd <= 0) return;

        int idx = indexOf(key);
        if (idx == -1) {
            names.add(key);
            quantities.add(quantityToAdd);
        } else {
            quantities.set(idx, quantities.get(idx) + quantityToAdd);
        }
    }

    // Deducts the add-ons/customizable ingredients chosen by the customer
    public static void deductOrder(CustomerOrder order) {
        if (order == null) return;
        FoodItemNode current = order.getHead();
        while (current != null) {
            if (current.addOns != null && !current.addOns.isEmpty()) {
                for (String addOn : current.addOns.split(",")) {
                    decrementOne(addOn);
                }
            }
            current = current.next;
        }
    }

    private static void decrementOne(String rawName) {
        String key = normalize(rawName);
        int idx = indexOf(key);
        if (idx == -1) return;
        quantities.set(idx, Math.max(0, quantities.get(idx) - 1));
    }

    public static List<String> getIngredientNames() {
        return new ArrayList<>(names);
    }

    public static int getQuantity(String ingredientName) {
        int idx = indexOf(normalize(ingredientName));
        return (idx == -1) ? 0 : quantities.get(idx);
    }

    public static String getStatusLabel(int qty) {
        if (qty <= 0) return "OUT";
        if (qty <= LOW_STOCK_THRESHOLD) return "LOW";
        return "OK";
    }

    private static int indexOf(String key) {
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equalsIgnoreCase(key)) {
                return i;
            }
        }
        return -1;
    }

    private static String normalize(String s) {
        return (s == null) ? "" : s.trim();
    }
}
