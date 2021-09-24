package club.zzxn.jdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import javax.xml.transform.dom.DOMSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-19
 */
public class DemoTest {
    List<Student> students = Arrays.asList(
            new Student("张三",12),
            new Student("李四",32),
            new Student("王六", 22)
    );
    long startTime ;
    @Test
    public void test1(){
        List<Student> result = new ArrayList<>();
        for (Student student:students){
            if ("李四".equals(student.getName())){
                result.add(student);
            }
        }
        System.out.println(result);

    }


    //匿名内部类
    @Test
    public void test2(){
        List<Student> list = filter(students, new FilterProcess<Student>() {
            @Override
            public boolean process(Student student) {
                return student.getName().equals("李四");
            }
        });
        for (Student student : list) {
            System.out.println(student);
        }
    }
    public List<Student> filter(List<Student> students, FilterProcess<Student> filterProcess){
        List<Student> list = new ArrayList<>();
        for (Student student : students) {
            if(filterProcess.process(student)){
                list.add(student);
            }
        }
        return list;
    }

    public interface FilterProcess<T> {
        boolean process(T t);
    }

    @Test
    public void test3(){
        //串行
        students.stream().filter((e) -> e.getName().equals("李四"))
                .forEach(System.out::println);
        //并行
        students.parallelStream().filter((e) -> e.getName().equals("李四"))
                .forEach(System.out::println);

    }

    @Test
    public void test5() {
        List<String> list = new ArrayList<>();
        list.add("111 222 333");
        list.add("444 555 666");
        list.add("777 888 999");
        list = list.stream().map(s -> s.split(" ")).flatMap(Arrays::stream).collect(Collectors.toList());
        System.out.println(list); // [111, 222, 333, 444, 555, 666, 777, 888, 999]
    }
    @Test
    public void test6() {
        //满足任意一个
        boolean isHave = students.stream().anyMatch(student -> student.getAge() == 12);
        System.out.println(isHave); //true
        //全部满足
        isHave = students.stream().allMatch(student -> student.getAge() == 12);
        System.out.println(isHave); //false
        //都不满足
        isHave = students.stream().noneMatch(student -> student.getAge() == 13);
        System.out.println(isHave); // true
    }
    @Test
    public void test7() {
        Stream.iterate(0, n -> ++n).limit(5).forEach(System.out::println); //0 1 2 3 4
        System.out.println("---------");
        Stream.iterate(0, n -> ++n).parallel().limit(5).forEach(System.out::println); //2 3 1 0 4
        System.out.println("---------");
        Stream.iterate(0, n -> ++n).parallel().limit(5).forEachOrdered(System.out::println); //0 1 2 3 4
    }
    @Test
    public void test8() {
        int limit = 1_0000_00000;
        long now = System.currentTimeMillis();

        LongStream.rangeClosed(0, limit).parallel().reduce(0, Long::sum);
        System.out.println(System.currentTimeMillis() - now);

        now = System.currentTimeMillis();
        LongStream.rangeClosed(0, limit).reduce(0, Long::sum);
        System.out.println(System.currentTimeMillis() - now);

        now = System.currentTimeMillis();
        long sum = 0 ;
        for (long i = 0; i < limit; i++) {
            sum += i;
        }
        System.out.println(System.currentTimeMillis() - now);
    }

    @Test
    public void test09() {
        List<Integer> lists = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            lists.add(i);
        }
        startTime = System.currentTimeMillis();
        lists.forEach(i->{
            System.out.println("__________forEach____________");
        });
    }
    @Before
    public void before() {
        startTime = System.currentTimeMillis();
    }
    @After
    public void after() {
        long endTime = System.currentTimeMillis();
        System.out.println("time :" + (endTime - startTime));
    }
    @Test
    public void forMethod() {
        for (int i = 0; i < 10000; i++) {
            System.out.println("-------测试");
        }
    }

    private static void TestMethod() {
        try {
            Thread.sleep(1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void test10() {

    }
}
