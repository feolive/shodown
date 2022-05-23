package xyz.shodown.code.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import xyz.shodown.code.consts.CodeGenerateConsts;

/**
 * @ClassName: ServiceConfig
 * @Description: service输出配置
 * @Author: wangxiang
 * @Date: 2021/4/2 15:45
 */
public class ServiceConfig extends BaseFileOutConfig{
    public ServiceConfig(String templatePath) {
        super(templatePath);
    }

    @Override
    protected String getOutPath(TableInfo tableInfo) {
        String projectPackage = locateToProjectPackage(CodeGenerateConsts.SERVICE_TEMPLATE,true);
        return projectPackage + CodeGenerateConsts.SERVICE + getFocEnv().getTargetPath()
                + tableInfo.getServiceName() + StringPool.DOT_JAVA;
    }
}
