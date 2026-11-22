public class SalesReport {
    static int orderCount = 0;
    static double subtotal = 0;
    static double discounts = 0;
    static double vat = 0;
    static double total = 0;

    // adds a confirmed order
    public static void addSale(CustomerOrder order) {
        orderCount++;
        subtotal += order.getTotalPrice();
        discounts += order.getDiscount();
        vat += order.getVat();
        total += order.getFinalTotal();
    }

    // shows the money earned
    public static void display() {
        ScreenDisplay.clear();

        ScreenDisplay.boxTop("SALES REPORT");
        ScreenDisplay.boxRow("Orders confirmed", "" + orderCount);
        ScreenDisplay.boxSeparator();

        ScreenDisplay.boxRow("Subtotal", ScreenDisplay.money(subtotal));
        ScreenDisplay.boxRow("Discounts given", "-" + ScreenDisplay.money(discounts));
        ScreenDisplay.boxRow("VAT collected", ScreenDisplay.money(vat));
        ScreenDisplay.boxSeparator();

        ScreenDisplay.boxRow("TOTAL SALES", ScreenDisplay.money(total));
        ScreenDisplay.boxBottom();
        ScreenDisplay.pause();
    }

}