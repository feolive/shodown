package xyz.shodown.code.work;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import xyz.shodown.code.engine.BizCodeTemplateEngine;

/**
 * @ClassName: EnhancedGenerator
 * @Description: 扩展的generator
 * @Author: wangxiang
 * @Date: 2021/3/26 14:08
 */
@Slf4j
public class EnhancedGenerator extends AutoGenerator implements Generator {


    @Override
    public void execute() {
        log.info("==========================准备生成文件...==========================");
        // 初始化配置
        if (null == config) {
            config = new ConfigBuilder(getPackageInfo(), getDataSource(), getStrategy(), getTemplate(), getGlobalConfig());
            if (null != injectionConfig) {
                injectionConfig.setConfig(config);
            }
        }
        AbstractTemplateEngine engine = new BizCodeTemplateEngine();
        // 模板引擎初始化执行文件输出
        engine.init(this.pretreatmentConfigBuilder(config)).batchOutput().open();
        log.info("==========================文件生成完成！！！==========================");
    }

    @Override
    public <T> void loadConfig(T t) {
        if(t instanceof GlobalConfig){
            setGlobalConfig((GlobalConfig)t);
        }
        if(t instanceof DataSourceConfig){
            setDataSource((DataSourceConfig)t);
        }
        if(t instanceof InjectionConfig){
            setCfg((InjectionConfig)t);
        }
        if(t instanceof PackageConfig){
            setPackageInfo((PackageConfig)t);
        }
        if(t instanceof StrategyConfig){
            setStrategy((StrategyConfig)t);
        }
    }


}
