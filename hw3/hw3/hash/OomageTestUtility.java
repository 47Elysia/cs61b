package hw3.hash;

import java.util.HashSet;
import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        HashSet[] oomagelist = new HashSet[M];
        int[] size = new int[M];
        for (int i = 0; i < M; i += 1) {
            oomagelist[i] = new HashSet();
        }
        for (Oomage o : oomages) {
            int bucketsnum = (o.hashCode() & 0x7FFFFFFF) % M;
            oomagelist[bucketsnum].add(o);
            size[bucketsnum] += 1;
        }
        for (int i = 0; i < M; i += 1) {
            if (size[i] < oomages.size() / 50 || size[i] > oomages.size() / 2.5) {
                return false;
            }
        }
        return true;
    }
}
