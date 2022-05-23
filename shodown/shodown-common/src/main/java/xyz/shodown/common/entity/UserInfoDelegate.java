package xyz.shodown.common.entity;

import java.util.function.Function;

/**
 * @description: 用户信息代理
 * @author: wangxiang
 * @date: 2022/5/5 11:17
 */
public abstract class UserInfoDelegate<T> {

    /**
     * 获取用户信息的某项属性,即用户的详细
     * @param expression 所需获取的某项细节的表达式
     * @return 返回用户的某个属性
     */
    public <R> R getUserDetail(Function<T,R> expression){
        T userinfo = getUserInfo();
        return expression.apply(userinfo);
    }

    public abstract T getUserInfo();


}
