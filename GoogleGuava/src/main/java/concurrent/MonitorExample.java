package concurrent;

import com.google.common.util.concurrent.Monitor;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static java.lang.Thread.currentThread;

/**
 * ${DESCRIPTION}
 * 关于synchronized  ReentrantLock  Monitor用法
 * @author mengxp
 * @version 1.0
 * @create 2018-01-03 22:35
 **/
public class MonitorExample {

    public static void main(String[] args) {
        MapTest();
        //第一种方式采用LinkList+synchronized+wait()+notifyAll()
        //final SynchronQueue synchronQueue = new SynchronQueue();
        //第二种方式采用LinkList+ReentrantLock+Condition+await()+signalAll()
        //final LockCondition synchronQueue = new LockCondition();
        //第三种采用Guava的Monitor。前面两种方式都不是陈述式的编程，至少while这里看起来不舒服
        final GuavaMonitor synchronQueue = new GuavaMonitor();
        final AtomicInteger counter = new AtomicInteger(0);
        //生产线程
        for (int i = 0; i < 3; i++) {
            new Thread(
                    () -> {
                        for (; ; )//这个代表者死循环
                            try {
                                int data = counter.getAndIncrement();
                                synchronQueue.offer(data);
                                System.out.println("Producer" + currentThread() + " offer  " + data);
                                TimeUnit.MILLISECONDS.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    }
            ).start();
        }

        //消费者线程
        new Thread(() -> {
            for (; ; )
                try {
                    int take = synchronQueue.take();
                    System.out.println("Consumer" + currentThread() + " take element  " + take);
                    TimeUnit.MILLISECONDS.sleep(22);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }).start();
    }

    /**
     * Monitor.Guard+enterWhen+leave
     * 第三种方式 采用Monitor的方式
     */
    static class GuavaMonitor{
        private LinkedList<Integer> queue = new LinkedList<>();
        private int size = 10;
        private final Monitor monitor=new Monitor();
        private final Monitor.Guard CAN_OFFER= monitor.newGuard(()->queue.size()<size);
        private final Monitor.Guard CAN_TAKE= monitor.newGuard(()->!queue.isEmpty());

        private void offer(int value) {
            try {
                try {
                    monitor.enterWhen(CAN_OFFER);
                    queue.addLast(value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                   monitor.leave();
            }
        }

        private Integer take() {
            try {
                try {
                    monitor.enterWhen(CAN_TAKE);
                    return queue.removeFirst();
                } catch (InterruptedException e) {
                    //thorw RuntimeException时 不必要定义成员变量
                    throw new RuntimeException("Take ERROR");
                }
            } finally {
                monitor.leave();
            }
        }

    }

    /**
     * 第二种方式
     * 采用ReentrantLock+Condition+await+signalAll 实现生产者消费者模型
     */
    static class LockCondition {
        //默认非公平锁
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition FULL_CONDITION = lock.newCondition();
        private final Condition EMPTY_CONDITION = lock.newCondition();
        private LinkedList<Integer> queue = new LinkedList<>();
        private int size = 10;

        private void offer(int value) {
            try {
                lock.lock();
                while (queue.size() > size) {
                    try {
                        //队列满了 就await()  和synchronized中的wait()别混淆
                        FULL_CONDITION.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.addLast(value);
                //增加元素后，唤醒EMPTY_CONDITION
                EMPTY_CONDITION.signalAll();
            } finally {
                lock.unlock();
            }
        }


        private Integer take() {
            Integer result = null;
            try {
                lock.lock();
                while (queue.isEmpty()) {
                    try {
                        EMPTY_CONDITION.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                result = queue.removeFirst();
                FULL_CONDITION.signalAll();
            } finally {
                lock.unlock();
            }
            return result;
        }


        /**
         * java8 里面的consumer是接受一个参数，没有返回值
         *
         * @param consumer
         * @param t
         * @param <T>      比如在consumer中对T的属性赋值
         */
        private <T> void doAction(Consumer<T> consumer, T t) {
            try {
                lock.lock();
                consumer.accept(t);
            } finally {
                lock.unlock();
            }
        }

    }

    /**
     * 第一种方式
     *
     * @Jdk 1.5 的方式实现
     * 采用 LinkList+synchronized+wait()+notifyAll()实现一个指定容量的队列
     * 消费了通知生产者，生产了通知消费者
     */
    static class SynchronQueue {
        private LinkedList<Integer> queue = new LinkedList<>();
        private int size = 10;

        private void offer(int value) {
            synchronized (queue) {
                while (queue.size() >= size) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.addLast(value);
                queue.notifyAll();
            }
        }

        private Integer take() {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer integer = queue.removeFirst();
                queue.notifyAll();
                return integer;
            }
        }


    }



    /**
     * LinkedHashMap 和 hashMap区别
     */
    public static void MapTest() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("c", "200");
        hashMap.put("b", "300");
        hashMap.put("f", "400");
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("c", "200");
        linkedHashMap.put("b", "300");
        linkedHashMap.put("f", "400");
        //对两者分别取values() HashMap是采用对Key对字典顺排序->的得到值的集合
        Collection<String> mapValues = hashMap.values();
        //对两者分别取values() linkedHashMap是采用Link链表，put的先后顺序来得到集合
        Collection<String> linkValues = linkedHashMap.values();
        System.out.println(linkValues);
    }
}
