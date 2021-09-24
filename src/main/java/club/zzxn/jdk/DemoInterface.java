package club.zzxn.jdk;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-21
 */
interface A {
    void test1();

    default void test2() {
        System.out.println("------test2");
    }
    static void test3() {
        System.out.println("------test3");
    }
}
class B implements A {
    @Override
    public void test1() {

    }
    public static void main(String[] args) {
        A.test3();
    }
}

