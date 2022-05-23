package xyz.shodown.code.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import xyz.shodown.code.consts.CodeGenerateConsts;
import xyz.shodown.common.consts.Charsets;
import xyz.shodown.common.util.basic.MapUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName: BizCodeTemplateEngine
 * @Description: 业务雏形代码velocity生成引擎
 * @Author: wangxiang
 * @Date: 2021/3/26 11:27
 */
public class BizCodeTemplateEngine extends AbstractTemplateEngine {

    /**
     * velocity引擎
     */
    private VelocityEngine velocityEngine;

    public BizCodeTemplateEngine() {
        Properties p = new Properties();
        p.setProperty(Velocity.RESOURCE_LOADERS, CodeGenerateConsts.JAR);
        p.setProperty(CodeGenerateConsts.VM_LOAD_CLASS, CodeGenerateConsts.VM_LOAD_CLASS_VALUE);
        String jarPath = "jar:file:" + this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        p.setProperty(CodeGenerateConsts.VM_LOAD_PATH, jarPath);
        p.setProperty(Velocity.ENCODING_DEFAULT, Charsets.UTF8.name());
        p.setProperty(Velocity.INPUT_ENCODING, Charsets.UTF8.name());
        p.setProperty(CodeGenerateConsts.VM_LOAD_UNICODE, StringPool.TRUE);
        velocityEngine = new VelocityEngine(p);
    }

    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (StringUtils.isEmpty(templatePath)) {
            return;
        }
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        if(!MapUtil.isEmpty(objectMap)){
            TableInfo tableInfo = (TableInfo) objectMap.get("table");
            String entity = tableInfo.getEntityName();
            String facadeName = "I" + entity + "Facade";
            String facadeImplName = entity + "FacadeImpl";
            objectMap.put(CodeGenerateConsts.FACADE_NAME,facadeName);
            objectMap.put(CodeGenerateConsts.FACADE_IMPL_NAME,facadeImplName);
        }
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             OutputStreamWriter ow = new OutputStreamWriter(fos, ConstVal.UTF8);
             BufferedWriter writer = new BufferedWriter(ow)) {
            template.merge(new VelocityContext(objectMap), writer);
        }
        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }

    @Override
    public String templateFilePath(String filePath) {
        if (null == filePath || filePath.contains(CodeGenerateConsts.DOT_VM)) {
            return filePath;
        }
        return filePath + CodeGenerateConsts.DOT_VM;
    }

}
