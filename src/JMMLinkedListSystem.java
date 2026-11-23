import java.util.ArrayList;

class FoodItemNode {
    String itemName;
    double cookTime;
    double price;
    String addOns;
    AddOn[] ingredients;
    ArrayList<AddOn> chosenAddOns = new ArrayList<>();
    FoodItemNode next;

    public FoodItemNode(String itemName, double cookTime, double price) {
        this(itemName, cookTime, price, new AddOn[0]);
    }

    public FoodItemNode(String itemName, double cookTime, double price, AddOn[] ingredients) {
        this.itemName = itemName;
        this.cookTime = cookTime;
        this.price = price;
        this.addOns = "";
        this.ingredients = ingredients != null ? ingredients : new AddOn[0];
        this.next = null;
    }
}

class CustomerOrder {
    private FoodItemNode head;
    public int customerId; // 0 until confirmed
    private static int customerCounter = 1;

    boolean seniorOrPwd = false;
    boolean expedited = false;
    double vatRate = 0.12;
    double discountRate = 0.20;

    public CustomerOrder() {
        this.customerId = 0;
    }

    // gives ticket number
    public void assignTicketNumber() {
        if (customerId == 0) {
            customerId = customerCounter++;
        }
    }

    // head of the list
    public FoodItemNode getHead() {
        return head;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getItemCount() {
        int count = 0;
        FoodItemNode current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    // Adds order
    public void addFoodItem(MenuItem item) {
        FoodItemNode newNode = new FoodItemNode(
                item.getName(),
                item.getCookTime(),
                item.getPrice(),
                item.getIngredients()
        );
        if (head == null) {
            head = newNode;
        } else {
            FoodItemNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    // Adds order with chosen add-ons
    public void addFoodItem(MenuItem item, ArrayList<AddOn> addOns) {
        addFoodItem(item);
        FoodItemNode current = head;
        while (current.next != null) {
            current = current.next;
        }

        // price of this item becomes menu price + add-ons, e.g. "Cheese (+Php 10.00), Ice"
        ArrayList<String> labels = new ArrayList<>();
        for (AddOn a : addOns) {
            current.price += a.getPrice();
            if (a.getPrice() > 0) {
                labels.add(a.getName() + " (+" + ScreenDisplay.money(a.getPrice()) + ")");
            } else {
                labels.add(a.getName());
            }
        }
        current.addOns = String.join(", ", labels);
        current.chosenAddOns = addOns;
    }

    // removes item (starts at 1)
    public boolean removeItem(int number) {
        if (head == null || number < 1) {
            return false;
        }
        if (number == 1) {
            head = head.next;
            return true;
        }
        FoodItemNode current = head;
        for (int i = 1; i < number - 1 && current != null; i++) {
            current = current.next;
        }
        if (current == null || current.next == null) {
            return false;
        }
        current.next = current.next.next;
        return true;
    }

    // math for serving time
    public double getTotalCookTime() {
        double total = 0;
        FoodItemNode current = head;
        while (current != null) {
            total += current.cookTime;
            current = current.next;
        }
        return total;
    }

    // math for price
    public double getTotalPrice() {
        double total = 0;
        FoodItemNode current = head;
        while (current != null) {
            total += current.price;
            current = current.next;
        }
        return total;
    }

    public void setSeniorOrPwd(boolean value) {
        seniorOrPwd = value;
    }

    public boolean isSeniorOrPwd() {
        return seniorOrPwd;
    }

    public void setExpedited(boolean value) {
        expedited = value;
    }

    public boolean isExpedited() {
        return expedited;
    }

    // 20% off for senior or PWD
    public double getDiscount() {
        if (seniorOrPwd) {
            return getTotalPrice() * discountRate;
        }
        return 0;
    }

    // 12% VAT is added on top of the price, senior/PWD pay no VAT
    public double getVat() {
        if (seniorOrPwd) {
            return 0;
        }
        return getTotalPrice() * vatRate;
    }

    public double getFinalTotal() {
        return getTotalPrice() - getDiscount() + getVat();
    }

    @Override
    public String toString() {
        return "Order #" + customerId + (expedited ? " [EXPEDITED]" : "");
    }
}