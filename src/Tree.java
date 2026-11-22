import java.util.ArrayList;
import java.util.List;

public class Tree {
    TreeNode root;

    class TreeNode {
        TreeNode left;
        TreeNode right;
        CustomerOrder data;

        TreeNode() {
            left = null;
            right = null;
        }

        TreeNode(CustomerOrder data) {
            left = null;
            right = null;
            this.data = data;
        }
    }

    public Tree() {
        root = null;
    }

    boolean isEmpty() {
        return root == null;
    }

    private int compareOrders(CustomerOrder a, CustomerOrder b) {
        int timeCmp = Double.compare(a.getTotalCookTime(), b.getTotalCookTime());
        if (timeCmp != 0) {
            return timeCmp;
        }
        return Integer.compare(a.customerId, b.customerId);
    }

    void insert(CustomerOrder d) {
        root = insertRec(root, d);
    }

    public boolean removeById(int customerId) {
        CustomerOrder target = findById(root, customerId);
        if (target == null) {
            return false;
        }
        root = deleteRec(root, target);
        return true;
    }

    private CustomerOrder findById(TreeNode node, int customerId) {
        if (node == null) {
            return null;
        }
        if (node.data.customerId == customerId) {
            return node.data;
        }
        CustomerOrder leftMatch = findById(node.left, customerId);
        if (leftMatch != null) {
            return leftMatch;
        }
        return findById(node.right, customerId);
    }

    private TreeNode minValueNode(TreeNode node) {
        TreeNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        return 1 + Math.max(leftHeight, rightHeight);
    }

    private int getBalance(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    private TreeNode rightRotate(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        x.right = y;
        y.left = T2;

        return x;
    }

    private TreeNode leftRotate(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;

        y.left = x;
        x.right = T2;

        return y;
    }

    TreeNode insertRec(TreeNode root, CustomerOrder d) {
        if (root == null) {
            return new TreeNode(d);
        }

        int cmp = compareOrders(d, root.data);
        if (cmp < 0) {
            root.left = insertRec(root.left, d);
        } else if (cmp > 0) {
            root.right = insertRec(root.right, d);
        } else {
            return root;
        }

        int balance = getBalance(root);

        if (balance > 1 && compareOrders(d, root.left.data) < 0) {
            return rightRotate(root);
        }

        if (balance < -1 && compareOrders(d, root.right.data) > 0) {
            return leftRotate(root);
        }

        if (balance > 1 && compareOrders(d, root.left.data) > 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && compareOrders(d, root.right.data) < 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private TreeNode deleteRec(TreeNode root, CustomerOrder target) {
        if (root == null) {
            return null;
        }

        int cmp = compareOrders(target, root.data);
        if (cmp < 0) {
            root.left = deleteRec(root.left, target);
        } else if (cmp > 0) {
            root.right = deleteRec(root.right, target);
        } else {
            if (root.left == null || root.right == null) {
                root = (root.left != null) ? root.left : root.right;
            } else {
                TreeNode successor = minValueNode(root.right);
                root.data = successor.data;
                root.right = deleteRec(root.right, successor.data);
            }
        }

        if (root == null) {
            return null;
        }

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }

        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }

        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    void inorder() {
        inorderRec(root);
        System.out.println();
    }

    void inorderRec(TreeNode root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.data + " " + root.data.getTotalCookTime() + " ");
            inorderRec(root.right);
        }
    }

    CustomerOrder[] GetArray() {
        List<CustomerOrder> orders = new ArrayList<>();
        GetArrayRec(root, orders);
        return orders.toArray(new CustomerOrder[0]);
    }

    void GetArrayRec(TreeNode root, List<CustomerOrder> list) {
        if (root != null) {
            GetArrayRec(root.left, list);
            list.add(root.data);
            GetArrayRec(root.right, list);
        }
    }

    public void display() {
        if (root == null) {
            System.out.println("The tree is empty.");
            return;
        }
        printTree(root, "", true);
    }

    private void printTree(TreeNode node, String prefix, boolean isTail) {
        if (node != null) {
            String branch = "+--";
            System.out.println(prefix + branch + node.data + " (" + node.data.getTotalCookTime() + ")");

            String childPrefix = prefix + (isTail ? "    " : "|   ");
            printTree(node.left, childPrefix, node.right == null);
            printTree(node.right, childPrefix, true);
        }
    }
}
