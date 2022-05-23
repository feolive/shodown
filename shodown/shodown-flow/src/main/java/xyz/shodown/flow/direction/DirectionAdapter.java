package xyz.shodown.flow.direction;

import xyz.shodown.flow.evaluator.EvaluatorAdapter;
import xyz.shodown.flow.navigator.NavigatorAdapter;

/**
 * @ClassName: DirectionAdapter
 * @Description: 流程分支抽象适配
 * @Author: wangxiang
 * @Date: 2021/6/1 13:49
 */
public abstract class DirectionAdapter<T> implements Direction{

    /**
     * 入口判断器
     */
    private EvaluatorAdapter<T,?,?> entranceEvaluator;

    /**
     * 入口导航器
     */
    private NavigatorAdapter<T,?> entranceNavigator;

    /**
     * 调用入口判断器开始新的流程
     * @param t 参数
     * @throws Exception 异常
     */
    public void run(T t) throws Exception{
        this.entranceEvaluator.exec(t);
    }

    public EvaluatorAdapter<T, ?, ?> getEntranceEvaluator() {
        return entranceEvaluator;
    }

    public void setEntranceEvaluator(EvaluatorAdapter<T, ?, ?> entranceEvaluator) {
        this.entranceEvaluator = entranceEvaluator;
    }

    public NavigatorAdapter<T, ?> getEntranceNavigator() {
        return entranceNavigator;
    }

    public void setEntranceNavigator(NavigatorAdapter<T, ?> entranceNavigator) {
        this.entranceNavigator = entranceNavigator;
    }
}
