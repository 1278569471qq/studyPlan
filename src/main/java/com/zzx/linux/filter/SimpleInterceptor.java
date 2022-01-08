package com.zzx.linux.filter;

import net.hasor.core.MethodInterceptor;
import net.hasor.core.MethodInvocation;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-09-05
 */
public class SimpleInterceptor implements MethodInterceptor {
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Object returnData = invocation.proceed();
            return returnData;
        } catch (Exception e) {
            System.out.println("throw...");
            throw e;
        }
    }
}
