package com.zzx.hasor;


import java.lang.reflect.Method;
import java.util.function.Predicate;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.zzx.linux.filter.SimpleInterceptor;
import net.hasor.core.ApiBinder;
import net.hasor.core.DimModule;
import net.hasor.core.exts.aop.Matchers;
import net.hasor.db.JdbcModule;
import net.hasor.db.Level;
import net.hasor.spring.SpringModule;
/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-08-29
 */
@DimModule
@Component
public class ExampleModule implements SpringModule {

    private final DataSource dataSource;

    public ExampleModule(DataSource dataSource){
        this.dataSource = dataSource;
    }

    /**
     * Hasor 启动的时候会调用 loadModule 方法，
     * 在这里再把 DataSource 设置到 Hasor 中。
     * @param apiBinder
     * @throws Throwable
     */
    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        apiBinder.installModule(new JdbcModule(Level.Full, this.dataSource));
        //1.任意类
        Predicate<Class<?>> atClass = Matchers.anyClass();
        //2.任意方法
        Predicate<Method> atMethod = Matchers.anyMethod();
        //3.注册拦截器让@MyAop注解生效
        apiBinder.bindInterceptor(atClass, atMethod, new SimpleInterceptor());
    }
}
