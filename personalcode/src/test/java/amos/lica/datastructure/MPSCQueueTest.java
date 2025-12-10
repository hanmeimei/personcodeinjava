package amos.lica.datastructure;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

public class MPSCQueueTest {

    @Test
    public void test() throws InterruptedException {
        MultiProducerSingleConsumerQueue<String> queue = new MultiProducerSingleConsumerQueue<>();
        int producerCount = 3;
        int pCount = 10;
        for (int i = 0; i < producerCount; i++) {
            final int start = i;
            new Thread(() -> {
                int j = 0;
                while(j < pCount) {
                    queue.offer(start + ": " + j++);
                }
            }).start();
        }
        Thread consumer = new Thread(() -> {
            int count = producerCount * pCount;
            do {
                String v = queue.poll();
                if (v != null) {
                    System.out.println(v);
                    count--;
                }
            } while (count > 0);
        });
        consumer.start();
        consumer.join();
    }
}
