package xyz.shodown.code.entity;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName: FocEnv
 * @Description: FileOutConfig环境配置
 * @Author: wangxiang
 * @Date: 2021/4/2 14:50
 */
@Data
public class FocEnv {

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * java代码所在基础路径
     */
    private String javaBasePath;

    /**
     * 项目后缀
     */
    private Map<String,String> projectSuffix;

    /**
     * 目标文件夹
     */
    private String targetPath;

}
