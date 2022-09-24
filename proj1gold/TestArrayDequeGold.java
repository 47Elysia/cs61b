import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequeGold {
    @Test
    public void testaddFirst() {
        StudentArrayDeque<Integer> adq = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> bdq = new ArrayDequeSolution<Integer>();
        String log = new String();

        for (int i = 0; i < 1000; i += 1) {
            if (adq.size() == 0) {
                Integer x = StdRandom.uniform(2);
                Integer randnumber = StdRandom.uniform(1000);
                if (x == 0) {
                    adq.addFirst(randnumber);
                    bdq.addFirst(randnumber);
                    log += "addFirst(" + randnumber + ")\n";
                } else {
                    adq.addLast(randnumber);
                    bdq.addLast(randnumber);
                    log += "addLast(" + randnumber + ")\n";
                }
            } else {
                Integer x = StdRandom.uniform(4);
                Integer randnumber = StdRandom.uniform(1000);
                Integer aremove = 1;
                Integer bremove = 1;
                if (x == 0) {
                    adq.addFirst(randnumber);
                    bdq.addFirst(randnumber);
                    log += "addFirst(" + randnumber +")\n";
                } else if (x == 1) {
                    adq.addLast(randnumber);
                    bdq.addLast(randnumber);
                    log += "addLast(" + randnumber + ")\n";
                } else if (x == 2) {
                    aremove = adq.removeFirst();
                    bremove = bdq.removeFirst();
                    log += "removeFirst()\n";
                } else {
                    aremove = adq.removeLast();
                    bremove = bdq.removeLast();
                    log += "removeLast()\n";
                }
                assertEquals(log, bremove, aremove);
            }
        }
    }
}
