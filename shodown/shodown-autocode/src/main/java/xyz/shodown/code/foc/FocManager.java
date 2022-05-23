package xyz.shodown.code.foc;

import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import xyz.shodown.code.entity.FocEnv;

import java.util.List;

/**
 * @ClassName: FocManager
 * @Description: FileOutConfig配置管理接口类
 * @Author: wangxiang
 * @Date: 2021/4/2 14:19
 */
public interface FocManager {

    /**
     * 补充需要自定义输出的文件配置
     * @param configs 配置项
     */
    void supply(List<FileOutConfig> configs);

    /**
     * 准备需要的参数
     * @param focEnv 环境配置
     */
    void prepare(FocEnv focEnv);
}
