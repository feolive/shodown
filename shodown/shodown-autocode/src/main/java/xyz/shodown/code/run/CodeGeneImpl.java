package xyz.shodown.code.run;



import xyz.shodown.code.consts.CodeGenerateConsts;
import xyz.shodown.common.consts.Symbols;
import xyz.shodown.common.util.basic.MapUtil;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.common.util.io.ResourceUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: CodeGeneImpl
 * @Description: 代码生成实现类
 * @Author: wangxiang
 * @Date: 2021/3/24 16:49
 */
public class CodeGeneImpl implements CodeGeneManager {


    @Override
    public void output() throws IOException {
        CodeReactor reactor = new CodeReactor();
        Map<String,String> props = ResourceUtil.readProperties(CodeGenerateConsts.PROP_LOCATION);
        Map<String,String> initProps = reactor.init(props);
        if(!MapUtil.isEmpty(initProps)){
            props.putAll(initProps);
        }
        reactor.execute(facadeLocation(props));
    }

    /**
     * 获取facade路径
     */
    private Map<String,Object> facadeLocation(Map<String,String> props){
        Map<String,Object> injection = new HashMap<>();
        String basePackage = props.getOrDefault(CodeGenerateConsts.BASE_PACKAGE, Symbols.EMPTY_STR);
        String facadePack = basePackage + Symbols.FULL_STOP +"facade";
        String facadeImplPack = basePackage + Symbols.FULL_STOP +"facade.impl";
        String targetPack = props.get(CodeGenerateConsts.TARGET_PACKAGE);
        if(!StringUtil.isEmpty(targetPack)){
            facadePack = facadePack + Symbols.FULL_STOP + targetPack;
            facadeImplPack = facadePack + Symbols.FULL_STOP + "impl";
        }
        injection.put(CodeGenerateConsts.PACK_FACADE,facadePack);
        injection.put(CodeGenerateConsts.PACK_FACADE_IMPL,facadeImplPack);
        // 存入target-package
        injection.put(CodeGenerateConsts.TARGET_PACK,props.getOrDefault(CodeGenerateConsts.TARGET_PACKAGE,"default"));
        return injection;
    }

}
