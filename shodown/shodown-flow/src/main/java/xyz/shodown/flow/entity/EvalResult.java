package xyz.shodown.flow.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: EvalResult
 * @Description: 判断器处理返回结果实体
 * @Author: wangxiang
 * @Date: 2021/6/10 18:20
 */
@Data
public class EvalResult<Y,N> implements Serializable {

    /**
     * 判断结果,true肯定,false否定
     */
    private boolean result;

    /**
     * 肯定判断后,待传递的参数
     */
    private Y positiveData;

    /**
     * 否定判断后,待传递的参数
     */
    private N negativeData;

}
