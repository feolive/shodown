package xyz.shodown.code.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import xyz.shodown.code.consts.CodeGenerateConsts;
import xyz.shodown.common.consts.Symbols;

/**
 * @ClassName: FacadeImplConfig
 * @Description: facadeImpl输出配置
 * @Author: wangxiang
 * @Date: 2021/4/2 15:43
 */
public class FacadeImplConfig extends BaseFileOutConfig{
    public FacadeImplConfig(String templatePath) {
        super(templatePath);
    }

    @Override
    protected String getOutPath(TableInfo tableInfo) {
        String projectPackage = locateToProjectPackage(CodeGenerateConsts.FACADE_IMPL_TEMPLATE,true);
        return projectPackage + CodeGenerateConsts.FACADE + getFocEnv().getTargetPath() + "impl" + Symbols.SLASH
                + tableInfo.getEntityName() + "FacadeImpl" + StringPool.DOT_JAVA;
    }
}
