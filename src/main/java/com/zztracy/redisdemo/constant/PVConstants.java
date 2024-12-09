package com.zztracy.redisdemo.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 高并发文章pv计算案例的常量
 *
 * @author 詹泽
 * @since 2024/12/1 15:46
 */
public class PVConstants {
    public static final String CACHE_PV_LIST = "pv:list";
    public static final String CACHE_ARTICLE = "article:";
    public static final Map<Long, Map<Integer, Integer>> PV_MAP = new ConcurrentHashMap<>();
}
