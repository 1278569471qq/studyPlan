package club.zzxn.linux.filter;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-05
 */
@Component
@Slf4j
public class RequestFilter extends HandlerInterceptorAdapter {
    public static Set<String> ips = Sets.newConcurrentHashSet();
    @PostConstruct
    public void init() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request
            , HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteHost();
        log.info("访问，ip: {}, url :{}", ip, request.getRequestURI());
        if (!ips.contains(ip)) {
            log.info("无权限访问，ip: {}, url :{}", ip, request.getRequestURI());
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("没有权限访问此接口");
            TimeUnit.SECONDS.sleep(2);
            return false;
        }
        return true;
    }
}
