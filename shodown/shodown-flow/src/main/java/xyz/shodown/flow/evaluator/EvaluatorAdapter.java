package xyz.shodown.flow.evaluator;

import xyz.shodown.flow.entity.EvalResult;
import xyz.shodown.flow.navigator.NavigatorAdapter;

/**
 * @ClassName: EvaluatorAdapter
 * @Description: evaluator判断器抽象适配
 * @Author: wangxiang
 * @Date: 2021/6/1 13:51
 */
public abstract class EvaluatorAdapter<T,Y,N> implements Evaluator<T>{

    /**
     * 判断肯定时,下一部衔接的navigator
     */
    private NavigatorAdapter<Y,?> positiveNav;

    /**
     * 判断否定时,下一步衔接的navigator
     */
    private NavigatorAdapter<N,?> negativeNav;

    /**
     * 执行操作
     * @param t 原始参数
     * @throws Exception 异常
     */
    public void exec(T t) throws Exception{
        EvalResult<Y,N> evalResult = evaluate(t);
        boolean flag = evalResult.isResult();
        if(flag){
            if(this.positiveNav != null){
                Y data = evalResult.getPositiveData();
                positiveProcess(this.positiveNav,data);
            }
        }else{
            if(this.negativeNav != null){
                N data = evalResult.getNegativeData();
                negativeProcess(this.negativeNav,data);
            }
        }
    }

    /**
     * 肯定判断时的navigator逻辑处理
     * @param nav navigator
     * @param t 原始入参
     * @throws Exception 异常
     */
    private void positiveProcess(NavigatorAdapter<Y,?> nav,Y t) throws Exception{
        nav.process(t);
    }

    /**
     * 否定判断时的navigator逻辑处理
     * @param nav navigator
     * @param t 原始入参
     * @throws Exception 异常
     */
    private void negativeProcess(NavigatorAdapter<N,?> nav,N t) throws Exception{
        nav.process(t);
    }

    public NavigatorAdapter<Y, ?> getPositiveNav() {
        return positiveNav;
    }

    public void setPositiveNav(NavigatorAdapter<Y, ?> positiveNav) {
        this.positiveNav = positiveNav;
    }

    public NavigatorAdapter<N, ?> getNegativeNav() {
        return negativeNav;
    }

    public void setNegativeNav(NavigatorAdapter<N, ?> negativeNav) {
        this.negativeNav = negativeNav;
    }

    /**
     * 判断逻辑操作
     * @param arg 输入参数
     * @return 判断结果: true肯定,false否定
     * @throws Exception 异常
     */
    public abstract EvalResult<Y,N> evaluate(T arg) throws Exception;
}
