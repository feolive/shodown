package xyz.shodown.flow.navigator;


/**
 * @ClassName: Navigator
 * @Description:
 * <p>
 *     导航器接口(负责具体业务逻辑处理),被evaluator调用,同时也可以衔接新的direction和evaluator;
 *     其中泛型R为导航器处理结果类型
 * </p>
 * @Author: wangxiang
 * @Date: 2021/6/1 13:51
 */
public interface Navigator<R> {

}
