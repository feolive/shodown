package xyz.shodown.crypto.factory;


import xyz.shodown.crypto.enums.Algorithm;
import xyz.shodown.crypto.enums.CryptoErr;
import xyz.shodown.crypto.exception.ShodownCryptoException;
import xyz.shodown.crypto.handler.AlgorithmHandler;
import xyz.shodown.crypto.handler.AlgorithmHandlerAdapter;

/**
 * @ClassName: AlgorithmFactory
 * @Description: 具体算法工厂类
 * @Author: wangxiang
 * @Date: 2021/4/20 10:05
 */
public class AlgorithmFactory {

    /**
     * 获取算法实例
     * @param algorithm 枚举
     * @return 算法实例
     * @throws IllegalAccessException {@link IllegalAccessException}
     * @throws InstantiationException {@link InstantiationException}
     */
    public static AlgorithmHandler create(Algorithm algorithm) throws IllegalAccessException, InstantiationException {
        Class<?> cl = algorithm.getClazz();
        if(AlgorithmHandler.class.isAssignableFrom(cl)){
            return (AlgorithmHandler) cl.newInstance();
        }else {
            throw new ShodownCryptoException(CryptoErr.NOT_SUBCLASS_OF_ALGORITHM_HANDLER);
        }
    }

    /**
     * 获取算法处理实例
     * @param algorithm 算法
     * @return 算法处理实例
     * @throws IllegalAccessException {@link IllegalAccessException}
     * @throws InstantiationException {@link InstantiationException}
     */
    public static AlgorithmHandlerAdapter createAdapter(Algorithm algorithm) throws IllegalAccessException, InstantiationException {
        Class<?> cl = algorithm.getClazz();
        if(AlgorithmHandlerAdapter.class.isAssignableFrom(cl)){
            return (AlgorithmHandlerAdapter) cl.newInstance();
        }else {
            throw new ShodownCryptoException(CryptoErr.NOT_SUBCLASS_OF_ALGORITHM_ADAPTER);
        }
    }

}
