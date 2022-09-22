
public class LinkedListDeque<T> {
    private int size;
    private IntNode sentinel;

    public class IntNode {
        private IntNode prev;
        private T item;
        private IntNode next;
        private IntNode(IntNode prevNode, T currentitem, IntNode nextNode) {
            prev = prevNode;
            item = currentitem;
            next = nextNode;
        }
        private IntNode(IntNode prevNode, IntNode nextNode) {
            prev = prevNode;
            next = nextNode;
        }

    }

    public LinkedListDeque() {
        sentinel = new IntNode(null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addLast(T addlastitem) {
        IntNode lastlist = new IntNode(sentinel.prev, addlastitem, sentinel);
        sentinel.prev.next = lastlist;
        sentinel.prev = lastlist;
        size += 1;
    }
    public void addFirst(T addfirstitem) {
        IntNode firstlist = new IntNode(sentinel, addfirstitem, sentinel.next);
        sentinel.next = firstlist;
        sentinel.next.prev = firstlist;
        size += 1;
    }
    public int size() {
        return size;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T removefirstitem = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return removefirstitem;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T removelastitem = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return removelastitem;
    }
    public T get(int index) {
        if (index == 0){
            return sentinel.next.item;
        }
        IntNode sentinelplus = sentinel;
        while (index != 0 && sentinelplus.next.item != null) {
            sentinelplus = sentinelplus.next;
            index -= 1;
        }
        return sentinelplus.next.item;
    }
    public void printDeque() {
        IntNode sentinelx = sentinel;
        while (sentinelx.next != sentinel) {
            System.out.print(sentinel.next.item + " ");
            sentinelx = sentinelx.next;
        }
    }
    private T recursivehelp(IntNode node, int x) {
        if (x == 0) {
            return node.item;
        }
        else {
            return recursivehelp(node.next, x - 1);
        }
    }

    public T getRecursive(int index) {
        if (index >= size ) {
            return null;
        }
        return recursivehelp(sentinel, size);
    }
}
