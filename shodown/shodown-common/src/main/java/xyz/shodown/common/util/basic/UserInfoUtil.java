package xyz.shodown.common.util.basic;

import cn.hutool.extra.spring.SpringUtil;
import xyz.shodown.common.entity.UserInfoDelegate;

import java.util.function.Function;

/**
 * @ClassName: UserInfoUtil
 * @Description: 用户信息工具类
 * @Author: wangxiang
 * @Date: 2021/8/23 15:52
 */
public class UserInfoUtil {

    /**
     * 动态适配并获取用户代理对象
     * @return
     */
    private static <T> UserInfoDelegate<T> getDelegate(Class<T> userInfoType){
        String[] beanNames = SpringUtil.getBeanNamesForType(UserInfoDelegate.class);
        if(ArrayUtil.isEmpty(beanNames)){
            return null;
        }else {
            return SpringUtil.getBean(beanNames[0]);
        }
    }

    /**
     * 返回用户信息
     * @param userInfoType 用户对象实体类
     * @return 当前登录用户信息
     */
    public static <T> T getUserInfo(Class<T> userInfoType) {
        UserInfoDelegate<T> delegate = getDelegate(userInfoType);
        return delegate==null?null:delegate.getUserInfo();
    }

    /**
     * 返回用户某个属性
     * @param expression 属性的get方法
     * @param <T> 用户信息类
     * @param <R> 属性返回的类型
     * @return 用户某项信息
     */
    public static <T,R> R getUserDetail(Class<T> userInfoType,Function<T,R> expression){
        UserInfoDelegate<T> delegate = getDelegate(userInfoType);
        return delegate==null?null:delegate.getUserDetail(expression);
    }

}
