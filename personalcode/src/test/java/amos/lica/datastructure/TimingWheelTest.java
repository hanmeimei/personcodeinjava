package amos.lica.datastructure;

import org.junit.jupiter.api.Test;

import java.time.Instant;

public class TimingWheelTest {

    @Test
    public void test() throws InterruptedException {
        TimingWheel wheel = new TimingWheel();
        wheel.start();
        Instant start = Instant.now();
        for (int i = 0; i < 5; i++) {
            final String producer = "p-" + (i + 1) + ":";
            new Thread(() -> {
                wheel.addTask(new TimingWheel.DelayTask(start.toEpochMilli() + 5010, ()-> {
                    System.out.println(producer + Instant.now() + ": " + start.toEpochMilli() + 5010);
                }));
                wheel.addTask(new TimingWheel.DelayTask(start.toEpochMilli() + 5120, ()-> {
                    System.out.println(producer + Instant.now() + ": " + start.toEpochMilli() + 5120);
                }));

                wheel.addTask(new TimingWheel.DelayTask(start.toEpochMilli() + 5320, ()-> {
                    System.out.println(producer + Instant.now() + ": " + start.toEpochMilli() + 5320);
                }));
            }).start();
        }

        Thread.sleep(15000);
    }
}
