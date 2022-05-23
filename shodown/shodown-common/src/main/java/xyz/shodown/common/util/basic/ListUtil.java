package xyz.shodown.common.util.basic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ListUtil
 * @Description: List工具
 * @Author: wangxiang
 * @Date: 2021/2/4 18:33
 */
public class ListUtil extends cn.hutool.core.collection.ListUtil{

    /**
     * Object转list
     * @param <T> 泛型
     * @param obj obj
     * @param clazz 元素类型
     * @return list
     */
    public static <T> List<T> fromObject(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 判断list是否为空
     * @param list 待判断list
     * @return 空返回true,不为空返回false
     */
    public static boolean isEmpty(List<?> list){
        return list == null || list.size() == 0;
    }

    /**
     * List转数组
     * @param list LIST
     * @param <T> 元素类型
     * @return 元素数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(List<T> list){
        if(isEmpty(list)){
            return null;
        }
        T t = list.get(0);
        Class<?> cls = t.getClass();
        T[] tArr = (T[])Array.newInstance(cls,list.size());
        return list.toArray(tArr);
    }

    /**
     * 剔除LIST中的空元素
     * @param <T> 元素类型
     * @return 剔除后的LIST,如果元素都为空,则返回null
     */
    public static <T> List<T> trim(List<T> list){
        if(isEmpty(list)){
            return null;
        }
        if(list.contains(null)){
            List<T> res = new ArrayList<>();
            for (T t : list) {
                if(t!=null){
                    res.add(t);
                }
            }
            return res;
        }else{
            return list;
        }
    }


}
