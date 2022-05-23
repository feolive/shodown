package xyz.shodown.code.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import xyz.shodown.code.consts.CodeGenerateConsts;

/**
 * @ClassName: MapperConfig
 * @Description: Mapper输出配置
 * @Author: wangxiang
 * @Date: 2021/4/2 14:26
 */
public class MapperConfig extends BaseFileOutConfig{

    public MapperConfig(String templatePath) {
        super(templatePath);
    }

    @Override
    protected String getOutPath(TableInfo tableInfo) {
        String projectPackage = locateToProjectPackage(CodeGenerateConsts.MAPPER_TEMPLATE,true);
        return projectPackage + CodeGenerateConsts.DAO + getFocEnv().getTargetPath()
                + tableInfo.getMapperName() + StringPool.DOT_JAVA;
    }

}
