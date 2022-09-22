public class ArrayDeque<T> {
    private int size;
    private int length;
    private int front;
    private int last;
    private T[] items;
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        length = 8;
        front = 3;
        last = 4;
    }
    private int plusone(int index, int modulo) {
        index = index % modulo;
        if (index == modulo - 1) {
            return 0;
        }
        return index + 1;
    }
    private int minusone(int index) {
        if (index == 0) {
            return length - 1;
        }
        return index - 1;
    }

    private void big() {
        T[] t = (T[]) new Object[2 * length];
        int st1 = plusone(front, length);
        int st2 = length + 1;
        while (st1 != last) {
            t[st2] = items[st1];
            st1 = plusone(st1, length);
            st2 = plusone(st2, length * 2);
        }
        last = st2;
        front = length;
        items = t;
        length *= 2;
    }
    private void small() {
        T[] t = (T[]) new Object[length / 2];
        int st1 = front + 1;
        int st2 = length / 4;
        while (st1 != last) {
            t[st2] = items[st1];
            st1 = plusone(st1, length);
            st2 = plusone(st2, length / 2);
        }
        last = st2;
        front = length / 4 - 1;
        items = t;
        length /= 2;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    public void addLast(T aLast) {
        if (size == length - 1) {
            big();
        }
        items[last] = aLast;
        last = plusone(last, length);
        size += 1;
    }
    public void addFirst(T first) {
        if (size == length - 1) {
            big();
        }
        items[front] = first;
        front = minusone(front);
        size += 1;
    }
    public T removeLast() {
        if (length >= 16 && length / size >= 4) {
            small();
        }
        if (size == 0) {
            return null;
        }
        T x;
        last = minusone(last);
        x = items[last];
        items[last] = null;
        size -= 1;
        return x;
    }
    public T removeFirst() {
        if (length >= 16 && length / size >= 4) {
            small();
        }
        if (size == 0) {
            return null;
        }
        T x;
        front = plusone(front, length);
        x = items[front];
        items[front] = null;
        size -= 1;
        return x;
    }
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int first = plusone(front, length);
        while (index != 0) {
            first = plusone(first, length);
            index -= 1;
        }
        return items[first];
    }
    public int size() {
        return size;
    }

    public void printDeque() {
        int first = front + 1;
        while (first != last - 1) {
            if (items[first] == null) {
                return;
            }
            System.out.print(items[first] + " ");
            first = plusone(first, length);
        }
        if (items[first] == null) {
            return;
        }
        System.out.print(items[first] + " ");
    }
}
