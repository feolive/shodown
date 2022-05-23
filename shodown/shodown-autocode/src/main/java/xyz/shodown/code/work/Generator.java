package xyz.shodown.code.work;

/**
 * @ClassName: Generator
 * @Description:
 * @Author: wangxiang
 * @Date: 2021/3/26 14:07
 */
public interface Generator {

    /**
     * 执行操作
     */
    void execute();

    /**
     * 装载配置
     * @param t 配置项
     * @param <T> 配置项类型
     */
    <T> void loadConfig(T t);
}
