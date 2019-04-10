package com.magicdogs.tablehelper;

import com.magicdogs.tablehelper.options.Options;

/**
 * @author magic
 * @date 2019/3/5/005 14:20
 * @version 1.0.0
 * Description TableNameModifyHelper
 */
public class TableNameHelper {

    private static final ThreadLocal<Options> SUFFIX_THREAD_LOCAL = new ThreadLocal<>();

    public static Options options(){
        Options options = new Options();
        SUFFIX_THREAD_LOCAL.set(options);
        return options;
    }

    public static Options take(){
        Options result = SUFFIX_THREAD_LOCAL.get();
        SUFFIX_THREAD_LOCAL.remove();
        return result;
    }

}
