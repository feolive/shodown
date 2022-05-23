package xyz.shodown.flow.spring.invoker;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import xyz.shodown.flow.context.ShodownFlowContext;

/**
 * @ClassName: ShodownFlowInvoker
 * @Description: 调用入口 T执行invoke时的参数类型
 * @Author: wangxiang
 * @Date: 2021/6/1 13:53
 */
public class ShodownFlowInvoker {

    /**
     * 初始化spring,在job的main方法中进行调用
     * @param jobClass job对应的class
     * @param args main方法对应的args
     * @throws Exception 异常
     */
    public static <T> void invoke(Class<?> jobClass,String[] args) throws Exception{
        SpringApplication springApplication = new SpringApplication(jobClass);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    /**
     * 执行流程
     * @param t 业务参数
     * @throws Exception
     */
    private static <T> void run(T t) throws Exception {
        ShodownFlowContext.getEntranceDirection().run(t);
    }

}
