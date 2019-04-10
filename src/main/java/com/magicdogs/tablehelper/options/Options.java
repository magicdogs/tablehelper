package com.magicdogs.tablehelper.options;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author magic
 * @version 1.0.0
 * Description Options
 * @date 2019/3/22/022 17:46
 */
public class Options {

    private String suffix;
    private Set<String> excludes;
    private Map<String,String> renames;

    public Options() {
        this.excludes = new HashSet<>(32);
        this.renames = new ConcurrentHashMap<>(32);
    }

    public String getSuffix() {
        return suffix;
    }

    public Options suffix(String suffix) {
        this.suffix = suffix;
        return this;
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


    public Options rename(String originName,String newName) {
        this.renames.put(originName,newName);
        return this;
    }

    public Map<String, String> getRenames() {
        return renames;
    }
}
