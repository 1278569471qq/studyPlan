package club.zzxn.jdk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-19
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String name;
    private int age;
}
