import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FoodMenu {
    private final String[] categoryNames = {
            "Burgers",
            "Chicken",
            "Sides",
            "Drinks",
            "Desserts"
    };
    private final List<MenuItem>[] categoryItems;

    @SuppressWarnings("unchecked")
    public FoodMenu() {
        categoryItems = (List<MenuItem>[]) new List[categoryNames.length];
        RestaurantMenu loader = new RestaurantMenu();

        for (int i = 0; i < categoryNames.length; i++) {
            String filePath = resolveFilePath(categoryNames[i] + ".txt");
            List<MenuItem> loaded = loader.loadMenuFromFile(filePath);
            categoryItems[i] = (loaded != null) ? loaded : new ArrayList<>();
        }
    }

    public static String resolveFilePath(String fileName) {
        File rootFile = new File(fileName);
        if (rootFile.exists()) {
            return rootFile.getPath();
        }

        File srcFile = new File("src", fileName);
        if (srcFile.exists()) {
            return srcFile.getPath();
        }

        File found = searchDirectory(new File(System.getProperty("user.dir")), fileName, 0);
        if (found != null) {
            return found.getPath();
        }

        try {
            URL resource = FoodMenu.class.getClassLoader().getResource(fileName);
            if (resource != null && "file".equalsIgnoreCase(resource.getProtocol())) {
                return Paths.get(resource.toURI()).toString();
            }
        } catch (Exception ignored) {
        }

        return fileName;
    }

    private static File searchDirectory(File dir, String targetName, int depth) {
        if (dir == null || !dir.isDirectory() || depth > 5) {
            return null;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return null;
        }

        for (File f : files) {
            if (f.isFile() && f.getName().equalsIgnoreCase(targetName)) {
                return f;
            }
        }
        for (File f : files) {
            if (f.isDirectory() && !f.getName().startsWith(".") && !f.getName().equals("out")) {
                File match = searchDirectory(f, targetName, depth + 1);
                if (match != null) {
                    return match;
                }
            }
        }
        return null;
    }

    public String[] getCategoryNames() {
        return categoryNames;
    }

    public List<MenuItem> getItems(int categoryIndex) {
        if (categoryIndex < 0 || categoryIndex >= categoryItems.length) {
            return new ArrayList<>();
        }
        return categoryItems[categoryIndex];
    }

    public void addItem(int categoryIndex, MenuItem item) {
        if (categoryIndex >= 0 && categoryIndex < categoryItems.length && item != null) {
            categoryItems[categoryIndex].add(item);
        }
    }

    public void removeItem(int categoryIndex, int itemIndex) {
        if (categoryIndex >= 0 && categoryIndex < categoryItems.length) {
            List<MenuItem> items = categoryItems[categoryIndex];
            if (itemIndex >= 0 && itemIndex < items.size()) {
                items.remove(itemIndex);
            }
        }
    }

    public static ArrayList<AddOn> chooseAddOns(MenuItem item) {
        ArrayList<AddOn> chosen = new ArrayList<>();
        if (item == null || item.getIngredients() == null) {
            return chosen;
        }

        List<AddOn> validOptions = new ArrayList<>();
        for (AddOn raw : item.getIngredients()) {
            if (raw != null && !raw.getName().trim().isEmpty()) {
                validOptions.add(raw);
            }
        }

        if (validOptions.isEmpty()) {
            return chosen;
        }

        while (true) {
            double addOnTotal = 0;
            for (AddOn a : chosen) {
                addOnTotal += a.getPrice();
            }

            ScreenDisplay.clear();
            ScreenDisplay.boxTop("CUSTOMIZE ADD-ONS");
            ScreenDisplay.boxCentered(item.getName());
            ScreenDisplay.boxSeparator();

            for (int i = 0; i < validOptions.size(); i++) {
                AddOn ing = validOptions.get(i);
                int stock = IngredientInventory.getQuantity(ing.getName());
                String check = chosen.contains(ing) ? "[x] " : "[ ] ";
                String status = (stock <= 0) ? "OUT OF STOCK" : (stock + " left");
                ScreenDisplay.boxRow("[" + (i + 1) + "] " + check + ing.getName(), priceLabel(ing) + "  " + status);
            }

            ScreenDisplay.boxSeparator();
            ScreenDisplay.boxRow("Add-ons total", ScreenDisplay.money(addOnTotal));
            ScreenDisplay.boxText("[0] Done / Confirm add-ons");
            ScreenDisplay.boxBottom();

            int choice = ScreenDisplay.readInt("Toggle add-on (0 when done):", 0, validOptions.size());
            if (choice == 0) {
                break;
            }

            AddOn picked = validOptions.get(choice - 1);
            if (!chosen.contains(picked) && IngredientInventory.getQuantity(picked.getName()) <= 0) {
                ScreenDisplay.print(picked.getName() + " is currently out of stock.");
                ScreenDisplay.pause();
            } else if (chosen.contains(picked)) {
                chosen.remove(picked);
            } else {
                chosen.add(picked);
            }
        }

        return chosen;
    }

    // "+Php 15.00" or "Free"
    private static String priceLabel(AddOn a) {
        if (a.getPrice() == 0) {
            return "Free";
        }
        return "+" + ScreenDisplay.money(a.getPrice());
    }
}