package xyz.shodown.boot.upms.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 前端组件权限vo
 * @author: wangxiang
 * @date: 2022/4/23 23:01
 */
@Data
public class ComponentVo implements Serializable {

    /**
     * 组件id
     */
    private String componentId;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 组件的权值
     */
    private String val;

    /**
     * 组件所属菜单
     */
    private String menuIds;

}
