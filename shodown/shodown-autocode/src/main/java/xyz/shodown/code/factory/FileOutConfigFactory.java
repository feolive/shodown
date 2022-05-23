package xyz.shodown.code.factory;


import xyz.shodown.code.config.*;
import xyz.shodown.code.consts.CodeGenerateConsts;
import xyz.shodown.code.entity.FocEnv;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: FileOutConfigFactory
 * @Description: FileOutConfig工厂类
 * @Author: wangxiang
 * @Date: 2021/4/2 15:18
 */
public class FileOutConfigFactory {

    private final Map<String,String> templates = new HashMap<>();

    {
        templates.put(CodeGenerateConsts.XML_TEMPLATE,CodeGenerateConsts.XML_TEMPLATE);
        templates.put(CodeGenerateConsts.MAPPER_TEMPLATE,CodeGenerateConsts.MAPPER_TEMPLATE);
        templates.put(CodeGenerateConsts.ENTITY_TEMPLATE,CodeGenerateConsts.ENTITY_TEMPLATE);
        templates.put(CodeGenerateConsts.CONTROLLER_TEMPLATE,CodeGenerateConsts.CONTROLLER_TEMPLATE);
        templates.put(CodeGenerateConsts.FACADE_TEMPLATE,CodeGenerateConsts.FACADE_TEMPLATE);
        templates.put(CodeGenerateConsts.FACADE_IMPL_TEMPLATE,CodeGenerateConsts.FACADE_IMPL_TEMPLATE);
        templates.put(CodeGenerateConsts.SERVICE_TEMPLATE,CodeGenerateConsts.SERVICE_TEMPLATE);
        templates.put(CodeGenerateConsts.SERVICE_IMPL_TEMPLATE,CodeGenerateConsts.SERVICE_IMPL_TEMPLATE);
    }

    /**
     * 创建config
     * @param cfgKey config键值
     * @return
     */
    public BaseFileOutConfig create(FocEnv focEnv, String cfgKey){
        Objects.requireNonNull(cfgKey);
        String template = templates.get(cfgKey);
        if(CodeGenerateConsts.XML_TEMPLATE.equals(cfgKey)){
            BaseFileOutConfig ins = new XmlConfig(template);
            ins.setFocEnv(focEnv);
            return ins;
        }
        if(CodeGenerateConsts.MAPPER_TEMPLATE.equals(cfgKey)){
            BaseFileOutConfig ins = new MapperConfig(template);
            ins.setFocEnv(focEnv);
            return ins;
        }
        if(CodeGenerateConsts.ENTITY_TEMPLATE.equals(cfgKey)){
            BaseFileOutConfig ins = new EntityConfig(template);
            ins.setFocEnv(focEnv);
            return ins;
        }
        if(CodeGenerateConsts.CONTROLLER_TEMPLATE.equals(cfgKey)){
            BaseFileOutConfig ins = new ControllerConfig(template);
            ins.setFocEnv(focEnv);
            return ins;
        }
        if(CodeGenerateConsts.FACADE_TEMPLATE.equals(cfgKey)){
            BaseFileOutConfig ins = new FacadeConfig(template);
            ins.setFocEnv(focEnv);
            return ins;
        }
        if(CodeGenerateConsts.FACADE_IMPL_TEMPLATE.equals(cfgKey)){
            BaseFileOutConfig ins = new FacadeImplConfig(template);
            ins.setFocEnv(focEnv);
            return ins;
        }
        if(CodeGenerateConsts.SERVICE_TEMPLATE.equals(cfgKey)){
            BaseFileOutConfig ins = new ServiceConfig(template);
            ins.setFocEnv(focEnv);
            return ins;
        }
        if(CodeGenerateConsts.SERVICE_IMPL_TEMPLATE.equals(cfgKey)){
            BaseFileOutConfig ins = new ServiceImplConfig(template);
            ins.setFocEnv(focEnv);
            return ins;
        }
        return null;
    }

}
