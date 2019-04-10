package com.magicdogs.tablehelper.autoconfigure;

import com.magicdogs.tablehelper.mybatis.InterceptorChainExtend;
import com.magicdogs.tablehelper.plugin.ModifyTableNameInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * @author magic
 * @date 2019/3/6/006 17:13
 * @version 1.0.0
 * Description TableNameModifyAutoConfiguration
 */
@Configuration
public class TableNameModifyAutoConfiguration implements ConfigurationCustomizer {

    private static final String INTERCEPTOR_CHAIN = "interceptorChain";

    public static Interceptor modifyTableNameInterceptor(){
        return new ModifyTableNameInterceptor();
    }

    @Override
    public void customize(org.apache.ibatis.session.Configuration configuration) {
        setConfiguration(configuration);
    }

    private static void setConfiguration(org.apache.ibatis.session.Configuration configuration) {
        InterceptorChainExtend interceptorChainExtend = new InterceptorChainExtend(modifyTableNameInterceptor());
        MetaObject metaObject = SystemMetaObject.forObject(configuration);
        metaObject.setValue(INTERCEPTOR_CHAIN,interceptorChainExtend);
    }

    public static org.apache.ibatis.session.Configuration generateConfiguration(){
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        setConfiguration(configuration);
        return configuration;
    }
}
