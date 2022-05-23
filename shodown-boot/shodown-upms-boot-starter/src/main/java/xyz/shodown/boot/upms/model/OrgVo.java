package xyz.shodown.boot.upms.model;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import xyz.shodown.common.entity.TreeNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 机构vo
 * @author: wangxiang
 * @date: 2022/4/23 23:10
 */
@Data
public class OrgVo implements TreeNode<String,OrgVo>,Serializable {

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构层级
     */
    private Integer orgLevel;

    /**
     * 父级机构id
     */
    private String parentId;

    /**
     * 子级机构
     */
    private List<OrgVo> children = new ArrayList<>();

    @JsonIgnore
    private Long sort;

    @Override
    public String fetchSelfId() {
        return this.orgId;
    }

    @Override
    public String fetchParentId() {
        return this.parentId;
    }

    @Override
    public List<OrgVo> fetchChildren() {
        return this.children;
    }

    @Override
    public Long fetchSort() {
        return this.sort;
    }
}
