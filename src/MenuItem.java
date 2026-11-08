public class MenuItem extends Food {
    private double price;

    public MenuItem(String name, double price, double cookTime, String[] ingredients) {
        super(name, cookTime, ingredients);
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getCookTime() {
        return cookTime;
    }

    public String[] getIngredients() {
        return customizableIngredients;
    }
}