import java.util.ArrayList;
import java.util.List;

public class Tree {
    TreeNode root;

    class TreeNode {
        TreeNode left;
        TreeNode right;
        CustomerOrder data;
        TreeNode(){
            left = null;
            right = null;
        }
        TreeNode(CustomerOrder data) {
            left = null;
            right = null;
            this.data = data;
        }
    }

    public Tree() { root = null; }
    boolean isEmpty() {
        return root == null;
    }
    void insert (CustomerOrder d) {
        root = insertRec(root, d);
    }

    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);

        if (leftHeight > rightHeight) {
            return 1 + leftHeight;
        } else {
            return 1 + rightHeight;
        }
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

    TreeNode insertRec (TreeNode root, CustomerOrder d) {
        if (root == null) {
            return new TreeNode(d);
        }

        if (d.getTotalCookTime() < root.data.getTotalCookTime()) {
            root.left = insertRec(root.left, d);
        } else if (d.getTotalCookTime() > root.data.getTotalCookTime()) {
            root.right = insertRec(root.right, d);
        } else {
            return root;
        }

        int balance = getBalance(root);

        if (balance > 1 && d.getTotalCookTime() < root.left.data.getTotalCookTime()) {
            return rightRotate(root);
        }

        if (balance < -1 && d.getTotalCookTime() > root.right.data.getTotalCookTime()) {
            return leftRotate(root);
        }

        if (balance > 1 && d.getTotalCookTime() > root.left.data.getTotalCookTime()) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && d.getTotalCookTime() < root.right.data.getTotalCookTime()) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    void inorder(){
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

            String childPrefix;
            childPrefix = prefix + "    ";
            if (!isTail) {
                childPrefix = prefix + "|   ";
            }

            printTree(node.left, childPrefix, node.right == null);
            printTree(node.right, childPrefix, true);
        }
    }
}