package xyz.shodown.common.util.basic;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import java.util.Map;

/**
 * @ClassName: MapUtil
 * @Description: map工具类,继承 {@link cn.hutool.core.map.MapUtil MapUtil}类
 * @Author: wangxiang
 * @Date: 2021/2/5 16:46
 */
public class MapUtil extends cn.hutool.core.map.MapUtil {

    /**
     * 对象转Map,K为String V为Object
     * 调用{@link BeanUtil#beanToMap(Object)}  BeanUtil.beanToMap}实现
     * 不进行驼峰转下划线,不忽略值为空的字段
     * @param obj
     * @return
     */
    public static Map<String,Object> fromObject(Object obj){
        return BeanUtil.beanToMap(obj);
    }

    /**
     * 将map转化为bean对象
     * 调用{@link BeanUtil#mapToBean(Map, Class, boolean, CopyOptions)}  BeanUtil.beanToMap}实现
     * @param map map
     * @param t 对象类型
     * @param <T> 泛型
     * @return 转换后的对象
     */
    public static <T> T toObject(Map<?,?> map,Class<T> t){
        return BeanUtil.mapToBean(map,t,true,null);
    }


}
