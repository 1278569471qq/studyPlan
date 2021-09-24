package club.zzxn.jdk;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-11
 */
public class FunctionalTest {
    public static void main(String[] args) {
        commonMethod(() -> System.out.println("张三 增加 自定义方法"));

        commonMethod(() -> System.out.println("李四 增加 自定义方法"));
    }

    public static void commonMethod(CommonFunction function) {
        System.out.println("执行 公共方法");
        if (function != null) {
            function.run();
        }
    }

    interface CommonFunction {
        void run(); //执行自己逻辑的方法
    }
}
