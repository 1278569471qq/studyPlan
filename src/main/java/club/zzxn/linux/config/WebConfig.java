package club.zzxn.linux.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import club.zzxn.linux.filter.RequestFilter;
import net.hasor.spring.boot.WebHasorConfiguration;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-05
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestFilter())
                .addPathPatterns("/**")
                .excludePathPatterns("/add/ip");
    }
}
