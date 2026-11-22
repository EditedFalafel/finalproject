import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortOrders {
    private final static Tree orderTree = new Tree();
    private static Queue<CustomerOrder> orderQueue = new Queue<>();
    private static final List<Integer> expeditedIds = new ArrayList<>();

    public void AddOrder(CustomerOrder head) {
        orderTree.insert(head);
        CastToQueue();
    }

    public void DisplayOrder() {
        System.out.println(Arrays.toString(orderTree.GetArray()));
        orderTree.display();
    }

    public void CastToQueue() {
        Queue<CustomerOrder> rebuiltQueue = new Queue<>();
        CustomerOrder[] orderArr = orderTree.GetArray();

        if (orderArr == null) {
            orderQueue = rebuiltQueue;
            return;
        }

        // 1. Place expedited orders at the front of the queue
        for (int expId : expeditedIds) {
            for (CustomerOrder order : orderArr) {
                if (order != null && order.customerId == expId) {
                    order.setExpedited(true);
                    rebuiltQueue.offer(order);
                    break;
                }
            }
        }

        // 2. Append remaining orders in AVL cook-time order
        for (CustomerOrder order : orderArr) {
            if (order == null || expeditedIds.contains(order.customerId)) {
                continue;
            }
            rebuiltQueue.offer(order);
        }

        orderQueue = rebuiltQueue;
    }

    public boolean expediteOrder(int customerId) {
        boolean found = false;
        for (CustomerOrder order : getActiveOrders()) {
            if (order.customerId == customerId) {
                order.setExpedited(true);
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }

        expeditedIds.remove(Integer.valueOf(customerId));
        expeditedIds.add(0, customerId);
        CastToQueue();
        return true;
    }

    public CustomerOrder completeNextOrder() {
        if (orderQueue == null || orderQueue.isEmpty()) {
            CastToQueue();
        }
        CustomerOrder next = orderQueue.poll();
        if (next != null) {
            expeditedIds.remove(Integer.valueOf(next.customerId));
            orderTree.removeById(next.customerId);
            CastToQueue();
        }
        return next;
    }

    public boolean deleteOrder(int customerId) {
        boolean removed = orderTree.removeById(customerId);
        if (removed) {
            expeditedIds.remove(Integer.valueOf(customerId));
            CastToQueue();
        }
        return removed;
    }

    public List<CustomerOrder> getActiveOrders() {
        if (orderQueue == null) {
            CastToQueue();
        }
        List<CustomerOrder> list = new ArrayList<>();
        Queue<CustomerOrder> temp = new Queue<>();

        while (!orderQueue.isEmpty()) {
            CustomerOrder current = orderQueue.poll();
            if (current != null) {
                list.add(current);
                temp.offer(current);
            }
        }
        orderQueue = temp;
        return list;
    }

    public Queue<CustomerOrder> getOrderQueue() {
        return orderQueue;
    }
}
