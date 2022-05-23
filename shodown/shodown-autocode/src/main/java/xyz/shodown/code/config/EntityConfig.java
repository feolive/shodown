package xyz.shodown.code.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import xyz.shodown.code.consts.CodeGenerateConsts;
import xyz.shodown.common.consts.Symbols;

/**
 * @ClassName: EntityConfig
 * @Description: Entity输出配置
 * @Author: wangxiang
 * @Date: 2021/4/2 14:26
 */
public class EntityConfig extends BaseFileOutConfig{

    public EntityConfig(String templatePath) {
        super(templatePath);
    }

    @Override
    protected String getOutPath(TableInfo tableInfo) {
        String projectPackage = locateToProjectPackage(CodeGenerateConsts.ENTITY_TEMPLATE,true);
        return projectPackage + CodeGenerateConsts.ENTITY + Symbols.SLASH + "po" + getFocEnv().getTargetPath()  + Symbols.SLASH
                + tableInfo.getEntityName() + StringPool.DOT_JAVA;
    }

}
