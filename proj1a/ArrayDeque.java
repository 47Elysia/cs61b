public class ArrayDeque<T> {
    private int size;
    private int length;
    private int nextfirst;
    private int nextlast;
    private T[] items;
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        length = 8;
        nextfirst = 3;
        nextlast = 4;
    }
    private int plusone(int index, int modulo) {
        if ((index + 1) % modulo == 0) {
            return 0;
        }
        return index % modulo;
    }
    private int minusone(int index) {
        if (index == 0) {
            return length;
        }
        return index - 1;
    }

    private void big() {
        T[] t = (T[]) new Object[2 * length];
        int st1 = nextfirst + 1;
        int st2 = length;
        while (st1 != nextlast){
            t[st2] = items[st1];
            st1 = plusone(st1, length);
            st2 = plusone(st2, length * 2);
        }
        nextlast = st2;
        nextfirst = length - 1;
        items = t;
        length *= 2;
    }
    private void small(){
        T[] t = (T[]) new Object[length / 2];
        int st1 = nextfirst + 1;
        int st2 = length / 4;
        while(st1 != nextlast){
            t[st2] = items[st1];
            st1 = plusone(st1, length);
            st2 = plusone(st2, length / 2);
        }
        nextlast = st2;
        nextfirst = length / 2 - 1;
        items = t;
        length /= 2;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    public void addLast(T last) {
        if (items[nextlast] != null) {
            big();
        }
        items[nextlast] = last;
        nextlast = plusone(nextlast, length);
        size += 1;
    }
    public void addFirst(T first) {
        if (items[nextfirst] != null) {
            big();
        }
        items[nextfirst] = first;
        nextfirst = minusone(nextfirst);
        size += 1;
    }
    public T removeLast() {
        if (length >= 16 && size / length <= 0.25){
            small();
        }
        if (size == 0){
            return null;
        }
        T x;
        nextlast = minusone(nextlast);
        x = items[nextlast];
        size -= 1;
        items[nextlast] = null;
        return x;
    }
    public T removeFirst() {
        if (length >= 16 && size / length <= 0.25){
            small();
        }
        if (size == 0){
            return null;
        }
        T x;
        nextfirst = plusone(nextfirst, length);
        x = items[nextfirst];
        size -= 1;
        items[nextfirst] = null;
        return x;
    }
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int first = nextfirst + 1;
        while (index != 0) {
            first += 1;
            index -= 1;
        }
        if (first >= length) {
            first -= length;
        }
        return items[first];
    }
    public int size() {
        return size;
    }

    public void printDeque() {
        int first = nextfirst + 1;
        while (nextfirst != nextlast) {
            if (items[first] == null) {
                return;
            }
            System.out.print(items[first] + " ");
            first = plusone(first,length);
        }
    }

}
