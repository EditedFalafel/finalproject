public abstract class Food {
    String name;
    double cookTime;
    String[] customizableIngredients;

    Food(String name, double cookTime, String[] customizableIngredients) {
        this.name = name;
        this.cookTime = cookTime;
        this.customizableIngredients = customizableIngredients;
    }
}
