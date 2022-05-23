package xyz.shodown.common.util.basic;

import lombok.extern.slf4j.Slf4j;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.entity.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 将查询出的结果转换为树形结构
 * @author: wangxiang
 * @date: 2022/4/19 16:49
 */
@Slf4j(topic = LogCategory.EXCEPTION)
public class TreeUtil {

    /**
     * 构建树形结构，不进行排序
     *
     * @param list 原查询结果集合
     * @param <T>  集合中元素的业务Id和parent_id类型
     * @param <R>  children的类型
     * @return 树形结果
     */
    public static <T, R extends TreeNode<T, R>> List<R> buildTree(List<R> list) {
        return buildTree(list, false);
    }

    /**
     * 构建树形结构
     *
     * @param list   原查询结果集合
     * @param isSort 是否对结果排序 false否 true是
     * @param <T>    集合中元素的业务Id和parent_id类型
     * @param <R>    children的类型
     * @return 树形结果
     */
    public static <T, R extends TreeNode<T, R>> List<R> buildTree(List<R> list, boolean isSort) {
        Map<T, R> dtoMap = new HashMap<>();
        for (R node : list) {
            dtoMap.put(node.fetchSelfId(), node);
        }
        List<R> resultList = new ArrayList<>();
        for (Map.Entry<T, R> entry : dtoMap.entrySet()) {
            R node = entry.getValue();
            if (node.fetchParentId() == null) {
                resultList.add(node);
            } else if (node.fetchParentId() instanceof String && StringUtil.isBlank((String) node.fetchParentId())) {
                // 如果是顶层节点，直接添加到结果集合中
                resultList.add(node);
            } else {
                // 如果不是顶层节点，找其父节点，并且添加到父节点的子节点集合中
                if (dtoMap.get(node.fetchParentId()) != null) {
                    List<R> children = dtoMap.get(node.fetchParentId()).fetchChildren();
                    if (children == null) {
                        throw new RuntimeException("请确保" + node.getClass() + "的fetchChildren对应的字段完成初始化！");
                    }
                    children.add(node);
                }
            }
        }
        if (isSort) {
            sorting(resultList);
        }
        return resultList;
    }

    /**
     * 排序集合内元素
     *
     * @param list 待排序集合
     * @param <T>  id类型
     */
    private static <T, R extends TreeNode<T, R>> void sorting(List<R> list) {
        if (!ListUtil.isEmpty(list) || list.size() > 1) {
            ListUtil.sort(list, list.get(0));
            for (R node : list) {
                List<R> children = node.fetchChildren();
                sorting(children);
            }
        }
    }

}
