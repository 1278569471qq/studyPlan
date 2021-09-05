package club.zzxn.linux.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.zzxn.linux.RunRemoteScript;
import club.zzxn.linux.filter.RequestFilter;
import club.zzxn.linux.filter.SimpleInterceptor;
import net.hasor.core.exts.aop.Aop;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-01
 */
@Aop(SimpleInterceptor.class)
@RestController
public class StartController {
    @RequestMapping("/start/linux")
    public void startLinux(HttpServletResponse response, int port){
        try {
            RunRemoteScript.startLinux(response, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/start")
    public void index(HttpServletResponse response){
        try {
            response.sendRedirect("start/linux?port=8080");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/ui")
    public void ui(HttpServletRequest request, HttpServletResponse response){
        try {
            response.sendRedirect("/config/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/add/ip")
    public String addIp(HttpServletRequest request){
        RequestFilter.ips.add(request.getRemoteHost());
        return request.getRemoteHost();
    }
}
