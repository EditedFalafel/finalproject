class FoodItemNode {
    String itemName;
    double cookTime;
    double price;
    FoodItemNode next;

    public FoodItemNode(String itemName, double cookTime, double price) {
        this.itemName = itemName;
        this.cookTime = cookTime;
        this.price = price;
        this.next = null;
    }
}

class CustomerOrder {
    private FoodItemNode head;
    public int customerId;
    private static int customerCounter = 1;

    public CustomerOrder() {
        this.customerId = customerCounter++;
    }

    // Adds order
    public void addFoodItem(MenuItem item) {
        FoodItemNode newNode = new FoodItemNode(item.getName(), item.getCookTime(), item.getPrice());
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

    public void displayOrderSummary() {
        System.out.println("\n--- Order Summary (Ticket #" + customerId + ") ---");
        FoodItemNode current = head;
        if (current == null) {
            System.out.println("No items ordered.");
            return;
        }
        while (current != null) {
            System.out.printf("- %s (Cook time: %.2f mins, Php%.2f)\n", current.itemName, current.cookTime, current.price);
            current = current.next;
        }
        System.out.println("-----------------------------------");
        System.out.printf("Total Amount: Php%.2f\n", getTotalPrice());
        System.out.println("Estimated Total Waiting Time: " + getTotalCookTime() + " mins");
    }
}