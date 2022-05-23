package xyz.shodown.common.util.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName: ArrayUtil
 * @Description: 数组工具类
 * @Author: wangxiang
 * @Date: 2021/3/30 11:28
 */
public class ArrayUtil extends cn.hutool.core.util.ArrayUtil {

    /**
     * 判断数组是否为空
     * @param t 数组
     * @param <T> 数组类型
     * @return true数组为空；false数组不为空
     */
    public static <T> boolean isEmpty(T[] t){
        if(t==null||t.length==0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 剔除数组中空元素
     * @param t 数组
     * @param <T> 元素类型
     * @return 剔除后的数组
     */
    public static <T> T[] trim(T[] t){
        if(t==null){
            return null;
        }
        List<T> temp = new ArrayList<>();
        for (T ele : t) {
            if(ele!=null){
                temp.add(ele);
            }
        }
        if(ListUtil.isEmpty(temp)){
            return null;
        }
        return ListUtil.toArray(temp);
    }

    /**
     * 数组转LIST
     * 注: 数组不为空,存在空数组元素的情况会导致LIST中存在null值;
     *     可以使用本工具中trim先过滤掉空元素,或使用{@code ListUtil.trim(list)}将返回结果进行剔除空元素
     * @param array 数组
     * @param <T> 元素类型
     * @return LIST
     */
    public static <T> List<T> toList(T[] array){
        if(isEmpty(array)){
            return null;
        }
        List<T> list = new ArrayList<>();
        Collections.addAll(list,array);
        return list;
    }

    /**
     * 判断数组是否包含某个元素
     * @param array 数组
     * @param t 指定元素
     * @param <T> 数组元素类型
     * @return true包含指定元素,false不包含指定元素
     */
    public static <T> boolean contains(T[] array,T t){
        return Arrays.asList(array).contains(t);
    }

}
