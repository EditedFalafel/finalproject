public class MenuItem {
    private double price;
    private String name;
    private double cookTime;
    private AddOn[] customizableIngredients;

    public MenuItem(String name, double price,  double cookTime, AddOn[] ingredients) {
        this.name = name;
        this.cookTime = cookTime;
        this.price = price;
        this.customizableIngredients = ingredients;
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

    public AddOn[] getIngredients() {
        return customizableIngredients;
    }
}