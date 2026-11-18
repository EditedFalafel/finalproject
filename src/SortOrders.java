import java.util.Arrays;

public class SortOrders {
    private final static Tree orderTree = new Tree();
    private static Queue<CustomerOrder> orderQueue;

    public void AddOrder(CustomerOrder head) {
        orderTree.insert(head);
        CastToQueue();
    }

    public void DisplayOrder() {
        System.out.println(Arrays.toString(orderTree.GetArray()));
        orderTree.display();
    }

    public void CastToQueue() {
        Queue<CustomerOrder> orderQueue = new Queue<>();
        CustomerOrder[] orderArr = orderTree.GetArray();

        for(CustomerOrder order : orderArr) {
            orderQueue.offer(order);
        }

        System.out.println(Arrays.toString(orderQueue.getArray()));
    }

}
