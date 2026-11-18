public class SortOrders {
    private final static Tree orderTree = new Tree();

    public void AddOrder(CustomerOrder head) {
        orderTree.insert(head);
    }

    public void DisplayOrder() {
        orderTree.inorder();
        orderTree.display();
    }
}
