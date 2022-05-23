package xyz.shodown.crypto.factory;


import xyz.shodown.crypto.enums.ProcessorEnum;
import xyz.shodown.crypto.processor.CryptoProcessor;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: ProcessorFactory
 * @Description: 处理器工厂类
 * @Author: wangxiang
 * @Date: 2021/4/20 11:04
 */
public class ProcessorFactory {

    /**
     * 实例缓存
     */
    private static final Map<String, CryptoProcessor> INSTANCES = new ConcurrentHashMap<>();

    /**
     * 获取处理器单例
     * @param processorEnum 处理器枚举
     * @return CryptoProcessor具体实现
     * @throws IllegalAccessException IllegalAccessException
     * @throws InstantiationException InstantiationException
     */
    public static CryptoProcessor getInstance(@NotNull ProcessorEnum processorEnum) throws IllegalAccessException, InstantiationException {
        CryptoProcessor processor = INSTANCES.get(processorEnum.getKey());
        if(processor==null){
            synchronized (ProcessorFactory.class){
                if(processor==null){
                    Class<?> cl = processorEnum.getClazz();
                    if(CryptoProcessor.class.isAssignableFrom(cl)){
                        processor = (CryptoProcessor) cl.newInstance();
                        INSTANCES.putIfAbsent(processorEnum.getKey(),processor);
                    }else {
                        throw new RuntimeException("所需实例化的实体不是CryptoProcessor子类");
                    }
                }
            }
        }
        return processor;
    }

}
