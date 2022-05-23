package xyz.shodown.core.syslog.aop;

import xyz.shodown.core.syslog.content.SysLogContent;


/**
 * @ClassName: SysLogSaver
 * @Description: 操作日志入库接口
 * @Author: wangxiang
 * @Date: 2021/8/23 14:01
 */
public interface SysLogSaver {

    /**
     * 保存日志至数据库中
     * @param content 日志内容
     * @return true保存成功,false保存失败
     * @throws Exception 异常
     */
    boolean save(SysLogContent content) throws Exception;

    /**
     * 是否是全局日志记录实例。
     * 1. 如果有多个实现类,并需要设置全局记录实例,则需要重写该方法并返回true,若不重写,则需要在每个@SysLog()
     * 2.
     * @return true是, false否。
     */
    default boolean isGlobal(){
        return false;
    }

}
