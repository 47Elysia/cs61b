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
    public void resize(int x){
        T[] t = (T[]) new Object[x];
        System.arraycopy(items, 0, t, 0, nextlast);
        System.arraycopy(items, nextlast, t, x - length + nextfirst - 1, length - nextlast );
        items = t;
        nextfirst = x - length + nextfirst;
        length = x;
    }
    public void addLast(T last){
        if (nextlast == length){
            nextlast = 0;
        }
        if (items[nextlast] != null){
            resize(2 * length);
        }
        items[nextlast] = last;
        nextlast += 1;
        size += 1;
    }
    public void addFirst(T first){
        if (nextfirst == -1){
            nextfirst = length - 1;
        }
        if (items[nextfirst] != null){
            resize(2 * length);
        }
        items[nextfirst] = first;
        nextfirst -= 1;
        size += 1;
    }
    public T removeLast(T last){
        T x;
        nextlast -= 1;
        if (nextlast == -1){
            nextlast = length - 1;
        }
        x = items[nextlast];
        size -= 1;
        items[nextlast] = null;
        return x;
    }
    public T removeFirst(T first){
        T x;
        nextfirst += 1;
        if (nextfirst == length){
            nextfirst = 0;
        }
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
