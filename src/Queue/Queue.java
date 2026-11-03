package Queue;

public class Queue<T> implements QueueInterface<T> {
    private T[] data;
    private int front;
    private int back;
    private int size;
    private int capacity;

    public Queue(int capacity) {
        this.capacity = capacity;
        this.data = (T[]) new Object[capacity];
        this.front = 0;
        this.back = -1;
        this.size = 0;
    }

    @Override
    public void offer(T item) {
        if (isFull()) {
            System.out.println("Queue is full.");
            return;
        }
        back = (back + 1) % capacity;
        data[back] = item;
        size++;
    }

    @Override
    public T poll() {
        if (isEmpty()) {
            return null;
        }
        T temp = data[front];
        data[front] = null;
        front = (front + 1) % capacity;
        size--;
        return temp;
    }

    @Override
    public T front() {
        if (isEmpty()) {
            return null;
        }
        return data[front];
    }

    @Override
    public T back() {
        if (isEmpty()) {
            return null;
        }
        return data[back];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean isFull() {
        return size == capacity;
    }
}