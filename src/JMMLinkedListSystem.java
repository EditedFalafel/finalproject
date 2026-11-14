class FoodItemNode {
    String itemName;
    double cookTime;
    double price;
    String addOns;
    FoodItemNode next;

    public FoodItemNode(String itemName, double cookTime, double price) {
        this.itemName = itemName;
        this.cookTime = cookTime;
        this.price = price;
        this.addOns = "";
        this.next = null;
    }
}

class CustomerOrder {
    private FoodItemNode head;
    public int customerId; // 0 until confirmed
    private static int customerCounter = 1;

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

    // Adds order with chosen add-ons
    public void addFoodItem(MenuItem item, String addOns) {
        addFoodItem(item);
        FoodItemNode current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.addOns = addOns;
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

    @Override
    public String toString() {
        return "Order #" + customerId;
    }
}