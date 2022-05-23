package xyz.shodown.code.foc;

import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import xyz.shodown.code.config.BaseFileOutConfig;
import xyz.shodown.code.consts.CodeGenerateConsts;
import xyz.shodown.code.entity.FocEnv;
import xyz.shodown.code.factory.FileOutConfigFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: DefaultFocManager
 * @Description: 默认FileOutConfig配置管理类
 * @Author: wangxiang
 * @Date: 2021/4/2 14:23
 */
public class DefaultFocManager implements FocManager{

    private final List<BaseFileOutConfig> baseFileOutConfigsonfigs = new ArrayList<>();

    @Override
    public void supply(List<FileOutConfig> configs) {
        Objects.requireNonNull(configs);
        configs.addAll(baseFileOutConfigsonfigs);
    }

    @Override
    public void prepare(FocEnv focEnv) {
        FileOutConfigFactory factory = new FileOutConfigFactory();
        baseFileOutConfigsonfigs.add(factory.create(focEnv, CodeGenerateConsts.XML_TEMPLATE));
        baseFileOutConfigsonfigs.add(factory.create(focEnv,CodeGenerateConsts.MAPPER_TEMPLATE));
        baseFileOutConfigsonfigs.add(factory.create(focEnv,CodeGenerateConsts.ENTITY_TEMPLATE));
        baseFileOutConfigsonfigs.add(factory.create(focEnv,CodeGenerateConsts.CONTROLLER_TEMPLATE));
        baseFileOutConfigsonfigs.add(factory.create(focEnv,CodeGenerateConsts.FACADE_TEMPLATE));
        baseFileOutConfigsonfigs.add(factory.create(focEnv,CodeGenerateConsts.FACADE_IMPL_TEMPLATE));
        baseFileOutConfigsonfigs.add(factory.create(focEnv,CodeGenerateConsts.SERVICE_TEMPLATE));
        baseFileOutConfigsonfigs.add(factory.create(focEnv,CodeGenerateConsts.SERVICE_IMPL_TEMPLATE));
    }
}
