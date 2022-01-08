package com.zzx.jdk;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-21
 */
public class ForkJoinDemo {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        SumRecursiveTask sumRecursiveTask = new SumRecursiveTask(1, 10000000L);
        Long result = pool.invoke(sumRecursiveTask);
        System.out.println(result);
    }
}
class SumRecursiveTask extends RecursiveTask<Long> {
    private static final long LIMIT_NUMBER = 1000L;
    private final long start;
    private final long end;
    public SumRecursiveTask(long start, long end) {
        this.start = start;
        this.end = end;
    }
    @Override
    protected Long compute() {
        long length = end - start;
        if (length < LIMIT_NUMBER) {
            long sum = 0;
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            //拆分
            long middle = (start + end) / 2;
            SumRecursiveTask left = new SumRecursiveTask(start, middle);
            left.fork();
            SumRecursiveTask right = new SumRecursiveTask(middle + 1, end);
            right.fork();

            return left.join() + right.join();
        }
    }

}
