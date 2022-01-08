package com.zzx.jdk;

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
    private String sName;
    private int sAge;
}
