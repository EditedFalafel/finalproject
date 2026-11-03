package Stack;

public class Stack implements StackInterface {
    int limit;
    int top;
    Object[] elements;

    public Stack(int limit) {
        this.limit = limit;
        this.top = -1;
        this.elements = new Object[limit];
    }

    @Override
    public void push(Object j) throws StackOverflowException {
        if(isFull()) {
            System.out.println("Stack is full");
            return;
        }
        top++;
        elements[top] = j;
    }

    @Override
    public void pop() throws StackUnderflowException {
        if(isEmpty()) {
            System.out.println("Stack is empty");
            return;
        }
        elements[top] = null;
        top--;
    }

    @Override
    public Object top() throws StackUnderflowException {
        return elements[top];
    }

    @Override
    public boolean isEmpty() {
        return top == -1;
    }

    @Override
    public boolean isFull() {
        return top == limit-1;
    }

    @Override
    public int size() {
        return top+1;
    }

    public void displayStack() {
        for(int i = top; i >= 0; i--) {
            System.out.println(elements[i]);
        }
    }

    public Object peek() {
        return elements[top];
    }
}