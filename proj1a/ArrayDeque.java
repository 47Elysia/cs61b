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
        int st1 = front + 1;
        int st2 = length;
        while (st1 != last - 1) {
            t[st2] = items[st1];
            st1 = plusone(st1, length);
            st2 = plusone(st2, length * 2);
        }
        t[st2] = items[st1];
        st2 = plusone(st2, length * 2);
        last = st2;
        front = length - 1;
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
        front = length / 2 - 1;
        items = t;
        length /= 2;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    public void addLast(T aLast) {
        if (items[last] != null) {
            big();
        }
        items[last] = aLast;
        last = plusone(last, length);
        size += 1;
    }
    public void addFirst(T first) {
        if (items[front] != null) {
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
        int first = front + 1;
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
    public static void main(String[] args){
        ArrayDeque x = new ArrayDeque();
        x.size();
        x.addLast(1);
        x.addFirst(2);
        x.addLast(3);
        x.addFirst(4);
        x.addFirst(5);
        x.addLast(6);
        x.addFirst(7);
        x.addFirst(8);
    }
}
