package Tree;

public class Tree {
    class TreeNode {
        TreeNode left;
        TreeNode right;
        String data;
        TreeNode(){
            left = null;
            right = null;
        }
        TreeNode(String data) {
            left = null;
            right = null;
            this.data = data;
        }
    }

    TreeNode root;
    public Tree() {
        root = null;
    }
    boolean isEmpty() {
        return root == null;
    }
    void insert (String d) {
        root = insertRec(root, d);
    }

    int WordToAsciiValue(String word) {
        int val = 0;
        for(char c : word.toCharArray()) {
            val += (int) c;
        }
        return val;
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

    TreeNode insertRec (TreeNode root, String d) {
        if (root == null) {
            return new TreeNode(d);
        }

        if (WordToAsciiValue(d) < WordToAsciiValue(root.data)) {
            root.left = insertRec(root.left, d);
        } else if (WordToAsciiValue(d) > WordToAsciiValue(root.data)) {
            root.right = insertRec(root.right, d);
        } else {
            return root;
        }

        int balance = getBalance(root);

        if (balance > 1 && WordToAsciiValue(d) < WordToAsciiValue(root.left.data)) {
            return rightRotate(root);
        }

        if (balance < -1 && WordToAsciiValue(d) > WordToAsciiValue(root.right.data)) {
            return leftRotate(root);
        }

        if (balance > 1 && WordToAsciiValue(d) > WordToAsciiValue(root.left.data)) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && WordToAsciiValue(d) < WordToAsciiValue(root.right.data)) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    void preorder() {
        preorderRec(root);
    }

    void inorder(){
        inorderRec(root);
    }

    void postorder() {
        postorderRec(root);
    }

    void inorderRec(TreeNode root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.data + " ");
            inorderRec(root.right);
        }
    }

    void preorderRec(TreeNode root) {
        if (root != null) {
            /* first print data of node */
            System.out.print(root.data + " ");
            /* then recur on left subtree */
            preorderRec(root.left);
            /* now recur on right subtree */
            preorderRec(root.right);
        }
    }

    void postorderRec(TreeNode root) {
        if (root != null) {
            /* then recur on left subtree */
            postorderRec(root.left);
            /* now recur on right subtree */
            postorderRec(root.right);
            /* first print data of node */
            System.out.print(root.data + " ");
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

            System.out.println(prefix + branch + node.data + " (" + WordToAsciiValue(node.data) + ")");

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