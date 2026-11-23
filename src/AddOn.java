import java.util.Locale;

// one add-on option plus how much it is
public class AddOn {
    private String name;
    private double price;   // 0 = free

    public AddOn(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    // no ":price" is free
    public static AddOn parse(String text) {
        String t = text.trim();
        int colon = t.lastIndexOf(':');
        if (colon == -1) {
            return new AddOn(t, 0);
        }
        double price = 0;
        try {
            price = Double.parseDouble(t.substring(colon + 1).replaceAll("[^0-9.]", "").trim());
        } catch (NumberFormatException ignored) {
        }
        return new AddOn(t.substring(0, colon).trim(), price);
    }

    public String toFileString() {
        if (price == Math.floor(price)) {
            return name + ":" + (int) price;
        }
        return name + ":" + String.format(Locale.US, "%.2f", price);
    }
}