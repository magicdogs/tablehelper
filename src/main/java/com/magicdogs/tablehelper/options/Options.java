package com.magicdogs.tablehelper.options;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author magic
 * @version 1.0.0
 * Description Options
 * @date 2019/3/22/022 17:46
 */
public class Options {

    private String suffix;
    private Set<String> excludes;

    public Options(String suffix) {
        this.suffix = suffix;
        this.excludes = new HashSet<>(32);
    }

    public String getSuffix() {
        return suffix;
    }

    public Options exclude(String table) {
        this.excludes.add(table);
        return this;
    }

    public Options excludes(String... tables) {
        this.excludes.addAll(Arrays.asList(tables));
        return this;
    }

    public Set<String> getExcludes() {
        return excludes;
    }

}
