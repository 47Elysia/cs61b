public class ArrayDeque <T>{
    private int size;
    private int length;
    private int nextfirst;
    private int nextlast;
    private T[] items;
    public ArrayDeque(){
        items = (T[]) new Object[8];
        size = 0;
        length = 8;
        nextfirst = 3;
        nextlast = 4;
    }
    public int plusone(int index){
        if (index == length - 1){
            return 0;
        }
        return index + 1;
    }
    public int minusone(int index){
        if (index == 0){
            return length;
        }
        return index - 1;
    }

    public void big(int x){
        T[] t = (T[]) new Object[x];
        System.arraycopy(items, 0, t, 0, nextlast);
        System.arraycopy(items, nextlast, t, x - length + nextfirst + 1, length - nextlast );
        items = t;
        nextfirst = x - length + nextfirst;
        length = x;
    }

    public boolean isEmpty(){
        return size == 0;
    }
    public void addLast(T last){
        if (items[nextlast] != null){
            big(2 * length);
        }
        items[nextlast] = last;
        nextlast = plusone(nextlast);
        size += 1;
    }
    public void addFirst(T first){
        if (items[nextfirst] != null){
            big(2 * length);
        }
        items[nextfirst] = first;
        nextfirst = minusone(nextfirst);
        size += 1;
    }
    public T removeLast(T last){
        T x;
        nextlast = minusone(nextlast);
        x = items[nextlast];
        size -= 1;
        items[nextlast] = null;
        return x;
    }
    public T removeFirst(T first){
        T x;
        nextfirst =plusone(nextfirst);
        x = items[nextfirst];
        size -= 1;
        items[nextfirst] = null;
        return x;
    }
    public T get(int index){
        if(index >= size){
            return null;
        }
        int first = nextfirst + 1;
        while(index != 0){
            first += 1;
            index -= 1;
        }
        if(first >= length){
            first -= length;
        }
        return items[first];
    }
    public int size(){
        return size;
    }

}
