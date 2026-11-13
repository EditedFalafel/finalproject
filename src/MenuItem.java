public class MenuItem {
    private double price;
    private String name;
    private double cookTime;
    private String[] customizableIngredients;

    public MenuItem(String name, double price,  double cookTime, String[] ingredients) {
        this.name = name;
        this.cookTime = cookTime;
        this.price = price;
        this.customizableIngredients = ingredients;
    }

    public MenuItem(String name, double price, double cookTime) {
        this.name = name;
        this.cookTime = cookTime;
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