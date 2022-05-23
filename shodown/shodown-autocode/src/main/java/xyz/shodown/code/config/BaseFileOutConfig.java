package xyz.shodown.code.config;

import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import xyz.shodown.code.consts.CodeGenerateConsts;
import xyz.shodown.code.entity.FocEnv;
import xyz.shodown.common.consts.Symbols;

import java.util.Objects;

/**
 * @ClassName: BaseFileOutConfig
 * @Description: 基础配置抽象类
 * @Author: wangxiang
 * @Date: 2021/4/2 14:46
 */
public abstract class BaseFileOutConfig extends FileOutConfig {

    /**
     * 环境配置
     */
    private FocEnv focEnv;

    public BaseFileOutConfig(String templatePath) {
        super(templatePath);
    }

    /**
     * 定位至项目包下的位置
     * @param isJava 是否生成java文件
     * @return
     */
    protected String locateToProjectPackage(String suffixKey,boolean isJava){
        Objects.requireNonNull(focEnv);
        Objects.requireNonNull(suffixKey);
        String pName = focEnv.getProjectName()+ Symbols.MINUS+focEnv.getProjectSuffix().get(suffixKey);
        String src = isJava? CodeGenerateConsts.SRC_JAVA + focEnv.getJavaBasePath() + Symbols.SLASH : CodeGenerateConsts.SRC_RESOURCE;
        return CodeGenerateConsts.PROJECT_PATH + Symbols.SLASH+ pName + src ;
    }

    @Override
    public String outputFile(TableInfo tableInfo) {
        return getOutPath(tableInfo);
    }

    /**
     * 获取输出路径
     * @param tableInfo
     * @return
     */
    protected abstract String getOutPath(TableInfo tableInfo);

    protected FocEnv getFocEnv(){
        return this.focEnv;
    }

    public void setFocEnv(FocEnv focEnv){
        this.focEnv = focEnv;
    }

}
