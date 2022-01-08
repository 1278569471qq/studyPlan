package com.zzx.linux;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-08-31
 */
public class test {
    //远程主机IP
    private static final String REMOTE_HOST = "www.zzxn.club";
    //远程主机用户名
    private static final String USERNAME = "root";
    //远程主机密码
    private static final String PASSWORD = "zzx337653788~";

    public static void main(String[] args) {

        String command = "source /etc/profile; sh /root/zzx/script/start.sh study.jar 8088 2>&1";
        Shell shell = new Shell(REMOTE_HOST, USERNAME, PASSWORD);
        String execCommand = shell.execCommand(command);
        System.out.println(execCommand);
    }
}
