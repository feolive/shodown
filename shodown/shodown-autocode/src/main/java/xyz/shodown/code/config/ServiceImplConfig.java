package xyz.shodown.code.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import xyz.shodown.code.consts.CodeGenerateConsts;
import xyz.shodown.common.consts.Symbols;

/**
 * @ClassName: ServiceImplConfig
 * @Description: serviceImpl输出配置
 * @Author: wangxiang
 * @Date: 2021/4/2 15:46
 */
public class ServiceImplConfig extends BaseFileOutConfig{
    public ServiceImplConfig(String templatePath) {
        super(templatePath);
    }

    @Override
    protected String getOutPath(TableInfo tableInfo) {
        String projectPackage = locateToProjectPackage(CodeGenerateConsts.SERVICE_IMPL_TEMPLATE,true);
        return projectPackage + CodeGenerateConsts.SERVICE + getFocEnv().getTargetPath() + "impl" + Symbols.SLASH
                + tableInfo.getServiceImplName() + StringPool.DOT_JAVA;
    }
}
