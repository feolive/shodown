package xyz.shodown.flow.navigator;

import xyz.shodown.flow.direction.DirectionAdapter;
import xyz.shodown.flow.evaluator.EvaluatorAdapter;

/**
 * @ClassName: NavigatorAdapter
 * @Description: navigator导航器抽象适配
 * @Author: wangxiang
 * @Date: 2021/6/1 13:52
 */
public abstract class NavigatorAdapter<T,R> implements Navigator<R>{

    /**
     * 衔接分支
     */
    private DirectionAdapter<R> direction;

    /**
     * 衔接判断器
     */
    private EvaluatorAdapter<R,?,?> evaluator;

    /**
     * navigator具体处理细节
     * @param t 参数
     * @throws Exception 异常
     */
    public void process(T t) throws Exception{
        R r = doProcess(t);
        if(direction!=null){
            // 如果direction存在,则直接进入direction流程,不会判断evaluator是否存在
            direction.run(r);
            return;
        }
        if(evaluator!=null){
            evaluator.exec(r);
        }
    }

    public DirectionAdapter<R> getDirection() {
        return direction;
    }

    public void setDirection(DirectionAdapter<R> direction) {
        this.direction = direction;
    }

    public EvaluatorAdapter<R, ?, ?> getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(EvaluatorAdapter<R, ?, ?> evaluator) {
        this.evaluator = evaluator;
    }

    /**
     * 实际处理环节
     * @param t 参数
     * @return 处理结果
     * @throws Exception 异常
     */
    public abstract R doProcess(T t) throws Exception;

}
