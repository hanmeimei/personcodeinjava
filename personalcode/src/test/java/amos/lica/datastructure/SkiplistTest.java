package amos.lica.datastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SkiplistTest {

    @Test
    void case1() {
        Skiplist skipList = new Skiplist();
        skipList.add(1);
        skipList.add(2);
        skipList.add(3);

        boolean sea = skipList.search(0);
        Assertions.assertEquals(sea, false);

        skipList.add(4);

        sea = skipList.search(1);
        Assertions.assertEquals(sea, true);

        boolean era = skipList.erase(0);
        Assertions.assertEquals(era, false);
        era = skipList.erase(1);
        Assertions.assertEquals(sea, true);

        sea = skipList.search(1);
        Assertions.assertEquals(sea, false);
    }

    @Test
    void case2() {
        Skiplist skipList = new Skiplist();
        skipList.add(1);
        skipList.add(2);
        skipList.add(3);

        skipList.search(0);

        skipList.add(4);
        skipList.search(1);

        skipList.erase(0);
        skipList.erase(1);
        skipList.search(1);
    }
}
