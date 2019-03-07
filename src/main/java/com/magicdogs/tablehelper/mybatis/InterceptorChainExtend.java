package com.magicdogs.tablehelper.mybatis;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;

import java.util.LinkedList;
import java.util.List;

/**
 * @author magic
 * @date 2019/3/6/006 17:25
 * @version 1.0.0
 * Description InterceptorChainExtend
 */
public class InterceptorChainExtend extends InterceptorChain {

    private Interceptor interceptor;

    public InterceptorChainExtend(Interceptor interceptor){
        this.interceptor = interceptor;
    }

    @Override
    public Object pluginAll(Object target) {
        Object parentTarget = super.pluginAll(target);
        return interceptor.plugin(parentTarget);
    }

    @Override
    public List<Interceptor> getInterceptors() {
        List<Interceptor> parentInterceptors = super.getInterceptors();
        List<Interceptor> interceptors = new LinkedList<>(parentInterceptors);
        interceptors.add(interceptor);
        return interceptors;
    }
}
