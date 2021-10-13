package club.zzxn.linux;

import static scala.Console.println;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-08-29
 */
public class RunRemoteScript {
    //远程主机IP
    private static final String REMOTE_HOST = "www.zzxn.club";
    //远程主机用户名
    private static final String USERNAME = "root";
    //远程主机密码
    private static final String PASSWORD = "zzx337653788~";
    //SSH服务端口
    private static final int REMOTE_PORT = 22;
    //会话超时时间
    private static final int SESSION_TIMEOUT = 10000;
    //管道流超时时间(执行脚本超时时间)
    private static final int CHANNEL_TIMEOUT = 5000;

    public static void startLinux(HttpServletResponse response, int port) throws IOException, JSchException {
        String command = "source /etc/profile; sh  /root/zzx/script/start.sh study.jar " + port +" 2>&1";
        JSch jsch = new JSch();
        Session session = jsch.getSession(USERNAME, REMOTE_HOST, 22);
        session.setPassword(PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(60 * 1000);
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        PipedInputStream pipeIn = new PipedInputStream();
        channel.setInputStream( pipeIn );
        InputStream in = channel.getInputStream();
        InputStream err = channel.getExtInputStream();
        channel.connect();
        byte[] tmp = new byte[1024];
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                String echo = new String(tmp, 0, i);
                writer.println(echo + "</br>");
                writer.flush();
            }
            while (err.available() > 0) {
                int i = err.read(tmp, 0, 1024);
                if (i < 0) break;
                writer.println(new String(tmp, 0, i));
                writer.flush();
            }
            if (channel.isClosed()) {
                if (in.available() > 0) continue;
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
        }
        writer.close();
        channel.disconnect();
        session.disconnect();
    }

    @Test
    public void start() throws Exception {
        String command = "source /etc/profile; sh  /root/zzx/script/start.sh study.jar " + 8089 +" 2>&1";
        JSch jsch = new JSch();
        Session session = jsch.getSession(USERNAME, REMOTE_HOST, 22);
        session.setPassword(PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(60 * 1000);
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        PipedInputStream pipeIn = new PipedInputStream();
        channel.setInputStream( pipeIn );
        InputStream in = channel.getInputStream();
        InputStream err = channel.getExtInputStream();
        channel.connect();
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                String echo = new String(tmp, 0, i);
                System.out.println(echo);
            }
            while (err.available() > 0) {
                int i = err.read(tmp, 0, 1024);
                if (i < 0) break;
                System.out.println(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                if (in.available() > 0) continue;
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
        }
        channel.disconnect();
        session.disconnect();
    }
}
