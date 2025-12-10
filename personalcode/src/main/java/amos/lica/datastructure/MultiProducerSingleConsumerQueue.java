package amos.lica.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

/**
 * 多个生产者写入，单个消费者消费。
 *
 * @param <T>
 */
public class MultiProducerSingleConsumerQueue<T> {

    private final AtomicReference<Node<T>> tail;
    private Node<T> head;

    public MultiProducerSingleConsumerQueue() {
        this.head = new Node<>(null);
        this.tail = new AtomicReference<>(head);
    }

    public void offer(T t) {
        Node<T> cur = new Node<>(t);
        Node<T> pre = tail.getAndSet(cur);
        pre.setNext(cur);
    }

    public T poll() {
        Node<T> cur = head.getNext();
        if (cur == null) {
            return null;
        }
        T d = cur.data;
        cur.data = null;
        head = cur;
        return d;
    }

    public List<T> filter(Predicate<T> predicate) {
        List<T> res = new ArrayList<>();
        Node<T> pre = head;
        Node<T> cur = head.getNext();
        while(cur != null) {
            if (predicate.test(cur.data)) {
                res.add(cur.data);
                pre.setNext(cur.next);
            } else {
                pre = cur;
            }
            cur = pre.getNext();
        }
        return res;
    }

    public static class Node<T> {
        T data;
        volatile Node<T> next;

        public Node(T data) {
            this.data = data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }
}
