package xyz.shodown.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: PageParam
 * @Description: 分页传参接收对象
 * @Author: wangxiang
 * @Date: 2021/7/8 20:21
 */
@Data
public class PageParam<T> implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 每页条目数
     */
    private int pageSize;

    /**
     * 查询条件
     */
    T condition;

}
