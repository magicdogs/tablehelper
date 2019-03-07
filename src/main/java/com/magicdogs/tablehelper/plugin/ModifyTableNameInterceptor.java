package com.magicdogs.tablehelper.plugin;

import com.magicdogs.tablehelper.sqlsource.ModifyTableSqlSource;
import com.magicdogs.tablehelper.TableNameHelper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author magic
 * @date 2019/3/5/005 14:19
 * @version 1.0.0
 * Description TableNameModifyInterceptor
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class,method = "update",args = {MappedStatement.class, Object.class })
})
public class ModifyTableNameInterceptor implements Interceptor {

    public static final String SQL_SOURCE = "sqlSource";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        String suffix = TableNameHelper.take();
        if(StringUtils.hasLength(suffix)){
            MappedStatement ms = (MappedStatement) args[0];
            SqlSource sqlSource = ms.getSqlSource();
            ModifyTableSqlSource modifyTableSqlSource = new ModifyTableSqlSource(sqlSource,suffix);
            MetaObject msObject = SystemMetaObject.forObject(ms);
            msObject.setValue(SQL_SOURCE, modifyTableSqlSource);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
