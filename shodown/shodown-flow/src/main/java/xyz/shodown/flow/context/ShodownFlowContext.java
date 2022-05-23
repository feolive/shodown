package xyz.shodown.flow.context;

import cn.hutool.extra.spring.SpringUtil;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.flow.direction.DirectionAdapter;

/**
 * @ClassName: ShodownFlowContext
 * @Description: ShodownFlow全局上下文
 * @Author: wangxiang
 * @Date: 2021/6/1 13:59
 */
public class ShodownFlowContext {

    /**
     * 是否已经设置过起始direction
     */
    private static boolean entranceDirectionAvailable = false;

    /**
     * 起始direction
     */
    private static volatile String entranceDirection;

    /**
     * 是否已经设置过起始direction
     * @return
     */
    public static boolean hasSetEntrance(){
        return ShodownFlowContext.entranceDirectionAvailable;
    }

    /**
     * 设置起始direction
     * @param entranceDirection
     */
    public static void setEntranceDirectionName(String entranceDirection){
        synchronized (ShodownFlowContext.class){
            if(StringUtil.isBlank(ShodownFlowContext.entranceDirection)){
                ShodownFlowContext.entranceDirection = entranceDirection;
                ShodownFlowContext.entranceDirectionAvailable = true;
            }
        }
    }

    /**
     * 获取起始direction
     */
    public static DirectionAdapter<Object> getEntranceDirection(){
        return SpringUtil.getBean(ShodownFlowContext.entranceDirection);
    }



}
