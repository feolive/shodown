package xyz.shodown.code.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import xyz.shodown.code.consts.CodeGenerateConsts;

/**
 * @ClassName: XmlConfig
 * @Description: XML输出配置
 * @Author: wangxiang
 * @Date: 2021/4/2 14:26
 */
public class XmlConfig extends BaseFileOutConfig{

    public XmlConfig(String templatePath) {
        super(templatePath);
    }

    @Override
    public String getOutPath(TableInfo tableInfo) {
        String projectPackage = locateToProjectPackage(CodeGenerateConsts.XML_TEMPLATE,false);
        return projectPackage + getFocEnv().getTargetPath() + tableInfo.getXmlName() + StringPool.DOT_XML;
    }

}
