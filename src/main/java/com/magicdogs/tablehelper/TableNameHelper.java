package com.magicdogs.tablehelper;

/**
 * @author magic
 * @date 2019/3/5/005 14:20
 * @version 1.0.0
 * Description TableNameModifyHelper
 */
public class TableNameHelper {
    private static final ThreadLocal<String> STRING_THREAD_LOCAL = new ThreadLocal<>();
    public static void suffix(String name){
        STRING_THREAD_LOCAL.set(name);
    }

    public static String take(){
        String result = STRING_THREAD_LOCAL.get();
        STRING_THREAD_LOCAL.remove();
        return result;
    }
}
