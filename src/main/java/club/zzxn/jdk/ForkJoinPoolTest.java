package club.zzxn.jdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-21
 */
public class ForkJoinPoolTest {
    //最大可用的CPU核数
    public static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws InterruptedException {
        //定义初始的2个数组
        List<Integer> firstRange = new ArrayList<>();
        List<Integer> secondRange = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            firstRange.add(i);
            secondRange.add(i);
        }
        System.out.println("poolParallelism :" + ForkJoinPool.getCommonPoolParallelism());

        //注意查看线程sleep时候，有几个线程在打印数据。特意sleep时间间隔较长
        test1(firstRange, secondRange);
//        test2(firstRange, secondRange);
        //保证程序一直存活的，打印数据
        while (true) {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println();
        }
    }

    //使用java8 parallelStream共享缓存
    public static void test1(List<Integer> firstRange, List<Integer> secondRange) {
        Runnable firstTask = () -> {
            firstRange.parallelStream().forEach((number) -> {
                try {
                    // do something slow
                    System.out.println("test1.1  --  >" + Thread.currentThread().getName() + "    num=" + number);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            });
        };

        Runnable secondTask = () -> {
            secondRange.parallelStream().forEach((number) -> {
                try {
                    // do something slow
                    System.out.println("test1.2  --  >" + Thread.currentThread().getName() + "    num=" + number);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            });
        };
        //new一个线程，模拟不同的请求，以免认为是顺序调用
        new Thread(new Runnable() {
            @Override
            public void run() {
                firstTask.run();
            }
        },"thread-111111").start();

        //new一个线程，模拟不同的请求，以免认为是顺序调用
        new Thread(new Runnable() {
            @Override
            public void run() {
                secondTask.run();
            }
        }, "thread-222222").start();

    }

    //包装线程池，不使用共享线程池，因为不是每个线程都必须是计算密集型的
    public static void test2(List<Integer> firstRange, List<Integer> secondRange) {
        //线程池大小可以根据cpu个数来定义
        ForkJoinPool forkJoinPool = new ForkJoinPool(PROCESSORS);
        forkJoinPool.submit(() -> {
            firstRange.parallelStream().forEach((number) -> {
                try {
                    System.out.println("test2.1  --  >" + Thread.currentThread().getName() + "    num=" + number);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            });
        });
        ForkJoinPool forkJoinPool2 = new ForkJoinPool(PROCESSORS);
        forkJoinPool2.submit(() -> {
            secondRange.parallelStream().forEach((number) -> {
                try {
                    System.out.println("test2.2  --  >" + Thread.currentThread().getName() + "    num=" + number);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            });
        });
    }
}
