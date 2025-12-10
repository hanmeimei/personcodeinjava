package amos.lica.datastructure;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class TimingWheel {
    private int wheelSpan;
    private int slotNumber;
    private int slotSpan; // 100ms for a slot.

    private long ticker;
    private final volatile long startTime;
    private final AtomicBoolean started;

    private final List<MultiProducerSingleConsumerQueue<DelayTask>> slots;
    private Thread trigger;
    private final ExecutorService executorService;


    public TimingWheel() {
        // 默认秒级时间轮
        this(1000, 10);
    }

    public TimingWheel(int wheelSpan, int slotNumer) {
        this.wheelSpan = wheelSpan;
        this.slotNumber = slotNumer;
        this.slotSpan = wheelSpan / slotNumer;
        this.ticker = 0;

        this.slots = new ArrayList<>();
        for (int i = 0; i < slotNumber; i++) {
            this.slots.add(new MultiProducerSingleConsumerQueue<>());
        }
        this.executorService = Executors.newFixedThreadPool(2);
        this.started = new AtomicBoolean(false);
    }

    private int index(long time) {
        return (int) ((time - startTime ) % wheelSpan / (slotSpan));
    }

    public void addTask(DelayTask task) {
        start();
        slots.get(index(task.deadline)).offer(task);
    }

    public void start() {
        if (!started.getAndSet(true)) {
            startTime = Instant.now().toEpochMilli();
            trigger = new Thread(() -> {
                while(started.get()) {
                    long expectMs = startTime + ticker * slotSpan;
                    if (Instant.now().toEpochMilli() < expectMs) {
                        LockSupport.parkUntil(expectMs);
                        if (!started.get()) {
                            break;
                        }
                    }
                    List<DelayTask> res = slots.get(index(expectMs)).filter(t -> t.deadline < expectMs + slotSpan);
                    res.forEach(r -> executorService.submit(r.task));
                    ticker++;
                }
            });
            trigger.start();
        }
    }

    public void stop() {
        started.set(false);
        if (trigger != null) {
            LockSupport.unpark(trigger);
        }
    }

    public static class DelayTask {
        long deadline;
        Runnable task;

        public DelayTask(long deadline, Runnable task) {
            this.deadline = deadline;
            this.task = task;
        }
    }
}
