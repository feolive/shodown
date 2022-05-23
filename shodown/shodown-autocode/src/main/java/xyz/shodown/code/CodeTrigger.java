package xyz.shodown.code;



import xyz.shodown.code.run.CodeGeneImpl;
import xyz.shodown.code.run.CodeGeneManager;

import java.io.IOException;

/**
 * @ClassName: CodeTrigger
 * @Description: 代码生成触发器
 * @Author: wangxiang
 * @Date: 2021/3/24 11:26
 */
public class CodeTrigger {

    /**
     * 生成代码
     * 在web项目中的配置文件code-generator.properties修改相应的配置
     * @throws IOException io异常
     */
    public static void trigger() throws IOException {
        CodeGeneManager manager = new CodeGeneImpl();
        manager.output();
    }

}
