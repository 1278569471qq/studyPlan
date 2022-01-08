package com.zzx.car;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-07-25
 */
public class test {
    public static void main(String[] args) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec("pwd; ls;");

        // 标准输入流（必须写在 waitFor 之前）
        String inStr = consumeInputStream(proc.getInputStream());
        // 标准错误流（必须写在 waitFor 之前）
        String errStr = consumeInputStream(proc.getErrorStream());

        int retCode = proc.waitFor();
        if(retCode == 0){
            System.out.println("程序正常执行结束");
        }
    }

    /**
     *   消费inputstream，并返回
     */
    public static String consumeInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s ;
        StringBuilder sb = new StringBuilder();
        while((s=br.readLine())!=null){
            System.out.println(s);
            sb.append(s);
        }
        return sb.toString();
    }
}
