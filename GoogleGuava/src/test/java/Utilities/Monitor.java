package Utilities;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.currentThread;

/**
 * ${DESCRIPTION}
 *
 * @author mengxp
 * @version 1.0
 * @create 2017-12-27 23:17
 **/
public class Monitor {


    /**
     * 采用JDK1.5 的做法实现了一个队列，先进先出
     * @param args
     */
    public static void main(String[] args) {
       final Syonied syonied=new Syonied();
       final AtomicInteger counter=new AtomicInteger(0);
       //生产线程
       for (int i=0;i<3;i++){

          new Thread(()->{
              for(;;)
              try {
                  int data=counter.getAndIncrement();
                  syonied.offer(data);
                  System.out.println("Producer"+currentThread()+" offer  "+data);
                  TimeUnit.MILLISECONDS.sleep(2);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }).start();
       }

       //消费线程
        new Thread(()->{
           for (;;)
            try {
                int take = syonied.take();
                System.out.println("Consumer"+currentThread()+" take element  "+take);
                TimeUnit.MILLISECONDS.sleep(22);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    static class Syonied {
        private LinkedList<Integer> queue = new LinkedList();
        private int size = 10;

        private void offer(int value) {
            synchronized (queue) {
                while (queue.size() >= size) {
                    try {
                        System.out.println("queue will runinto wait...");
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.addLast(value);
                queue.notifyAll();
            }
        }

        private int take() {
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
}
