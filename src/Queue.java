public class Queue<T> implements QueueInterface<T> {
    private T[] data;
    private int front;
    private int back;
    private int size;
    private int capacity = 1;

    public Queue() {
        this.data = (T[]) new Object[capacity];
        this.front = 0;
        this.back = -1;
        this.size = 0;
    }

    @Override
    public void offer(T item) {
        if (size == capacity) {
            int oldCapacity = capacity;
            capacity += 1;
            T[] tempArray = (T[]) new Object[capacity];

            for (int i = 0; i < size; i++) {
                tempArray[i] = data[(front + i) % oldCapacity];
            }
            data = tempArray;
            front = 0;
            back = size - 1;
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

    public T[] getArray() {
        if (isEmpty()) {
            return null;
        }
        return data;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
