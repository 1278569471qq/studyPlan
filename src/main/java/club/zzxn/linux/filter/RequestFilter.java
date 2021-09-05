package club.zzxn.linux.filter;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.collect.Sets;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-05
 */
@Component
public class RequestFilter extends HandlerInterceptorAdapter {
    public static Set<String> ips = Sets.newConcurrentHashSet();

    @Override
    public boolean preHandle(HttpServletRequest request
            , HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteHost();
        if (!ips.contains(ip)) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("没有权限访问此接口");
            return false;
        }
        return true;
    }
}
