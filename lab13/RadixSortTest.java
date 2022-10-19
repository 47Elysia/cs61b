import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RadixSortTest {
    @Test
    public void testsort() {
        String[] asciis = new String[]{"abd", "ky", "zj", "sf"};
        String[] sorted = RadixSort.sort(asciis);
        String[] expected = new String[]{"abd", "ky", "sf", "zj"};
        assertEquals(expected[0], sorted[0]);
        assertEquals(expected[1], sorted[1]);
        assertEquals(expected[2], sorted[2]);
        assertEquals(expected[3], sorted[3]);
    }
}