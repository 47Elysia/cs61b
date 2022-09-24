import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequeGold {
    @Test
    public void testaddFirst() {
        StudentArrayDeque<Integer> adq = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> bdq = new ArrayDequeSolution<Integer>();


        for (int i = 0; i < 10; i += 1) {
            double randnumber = StdRandom.uniform();

            if (randnumber > 0.5) {
                adq.addFirst(i);
                bdq.addFirst(i);
            } else {
                adq.addLast(i);
                bdq.addLast(i);
            }
        }

        for (int i = 0; i < 10; i += 1) {
            Integer expect = adq.removeLast();
            Integer actual = bdq.removeLast();
            assertEquals("Oh noooo!\nThis is bad:\n   Random number " + actual
                            + " not equal to " + expect + "!",
                    expect, actual);
        }
    }
}
