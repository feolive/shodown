package xyz.shodown.code.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import xyz.shodown.code.consts.CodeGenerateConsts;

/**
 * @ClassName: ControllerConfig
 * @Description: controller输出配置
 * @Author: wangxiang
 * @Date: 2021/4/2 15:39
 */
public class ControllerConfig extends BaseFileOutConfig{

    public ControllerConfig(String templatePath) {
        super(templatePath);
    }

    @Override
    protected String getOutPath(TableInfo tableInfo) {
        String projectPackage = locateToProjectPackage(CodeGenerateConsts.CONTROLLER_TEMPLATE,true);
        return projectPackage + CodeGenerateConsts.CONTROLLER + getFocEnv().getTargetPath()
                + tableInfo.getControllerName() + StringPool.DOT_JAVA;
    }
}
