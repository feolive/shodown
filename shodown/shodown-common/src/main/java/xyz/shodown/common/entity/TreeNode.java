package xyz.shodown.common.entity;

import java.util.Comparator;
import java.util.List;

/**
 * @description: 构建树形结构时,entity必须实现的接口定义
 * @author: wangxiang
 * @date: 2022/4/19 16:58
 */
public interface TreeNode<IdType,R extends TreeNode<IdType,R>> extends Comparator<TreeNode<IdType,R>> {

    /**
     * 标识自身的id字段
     */
    IdType fetchSelfId();

    /**
     * 父节点的id
     */
    IdType fetchParentId();

    /**
     * 标识自身的children字段
     */
    List<R> fetchChildren();

    /**
     * 排序字段
     */
    Long fetchSort();

    @Override
    default int compare(TreeNode<IdType,R> o1, TreeNode<IdType,R> o2){
        if(o1==null&&o2==null){
            return 0;
        }else if(o1==null){
            return -1;
        }else if(o2==null){
            return 1;
        }else{
            long s1 = o1.fetchSort();
            long s2 = o2.fetchSort();
            return Long.compare(s1, s2);
        }
    }
}
