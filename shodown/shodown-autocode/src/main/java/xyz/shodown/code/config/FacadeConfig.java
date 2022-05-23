package xyz.shodown.code.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import xyz.shodown.code.consts.CodeGenerateConsts;

/**
 * @ClassName: FacadeConfig
 * @Description: facade输出配置
 * @Author: wangxiang
 * @Date: 2021/4/2 15:42
 */
public class FacadeConfig extends BaseFileOutConfig{
    public FacadeConfig(String templatePath) {
        super(templatePath);
    }

    @Override
    protected String getOutPath(TableInfo tableInfo) {
        String projectPackage = locateToProjectPackage(CodeGenerateConsts.FACADE_TEMPLATE,true);
        return projectPackage + CodeGenerateConsts.FACADE + getFocEnv().getTargetPath()
                + "I" +tableInfo.getEntityName() + "Facade" + StringPool.DOT_JAVA;
    }
}
