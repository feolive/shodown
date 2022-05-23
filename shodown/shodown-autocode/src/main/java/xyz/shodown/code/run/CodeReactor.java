package xyz.shodown.code.run;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import xyz.shodown.code.consts.CodeErrMsg;
import xyz.shodown.code.consts.CodeGenerateConsts;
import xyz.shodown.code.entity.FocEnv;
import xyz.shodown.code.foc.DefaultFocManager;
import xyz.shodown.code.foc.FocManager;
import xyz.shodown.code.work.EnhancedGenerator;
import xyz.shodown.code.work.Generator;
import xyz.shodown.common.consts.StateConst;
import xyz.shodown.common.consts.Symbols;
import xyz.shodown.common.util.basic.MapUtil;
import xyz.shodown.common.util.basic.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CodeReactor
 * @Description: 基础业务代码生成类
 * @Author: wangxiang
 * @Date: 2021/3/19 11:27
 */
public class CodeReactor {

    /**
     * 生成器
     */
    private final Generator autoGenerator = new EnhancedGenerator();

    /**
     * 自定义参数
     */
    private final Map<String,Object> injectMap = new HashMap<>();

    /**
     * 是否进行过初始化, false否，true是
     */
    private boolean initSwitch = false;

    /**
     * 基础路径
     */
    private String basePath = "com/xhyj/";

    /**
     * 基本包路径
     */
    private String basePackage = "com.xhyj.";

    /**
     * 项目名称
     */
    private String projectName;

    private String author;

    private String url;

    private String driver;

    private String username;

    private String password;

    private String[] tables;

    private String ignorePrefix;

    private String targetPackage;

    private Boolean override;

    public CodeReactor() {
    }

    /**
     * 全局配置
     */
    private void loadGlobalConfig(){
        GlobalConfig gc = new GlobalConfig();

        // 输出路径
        gc.setOutputDir(CodeGenerateConsts.PROJECT_PATH);
        // 作者
        gc.setAuthor(author);
        // swagger信息
        gc.setSwagger2(true);
        gc.setOpen(false);
        // 日期类型定义为Date
        gc.setDateType(DateType.ONLY_DATE);
        // 是否覆盖
        gc.setFileOverride(override);
        // 配置模板
        autoGenerator.loadConfig(gc);
    }

    /**
     * 数据源配置
     */
    private void loadDataSource(){
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        dsc.setDriverName(driver);
        dsc.setUsername(username);
        dsc.setPassword(password);
        autoGenerator.loadConfig(dsc);
    }

    /**
     * 自定义配置
     * @param params 自定义参数
     */
    private void loadInjectConfig(Map<String,Object> params){
        if(!MapUtil.isEmpty(params)){
            this.injectMap.putAll(params);
        }
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                this.setMap(injectMap);
            }

        };

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        FocManager focManager = new DefaultFocManager();
        FocEnv focEnv = new FocEnv();
        focEnv.setProjectName(this.projectName);
        focEnv.setJavaBasePath(this.basePath);
        Map<String,String> suffix = new HashMap<>();
        suffix.put(CodeGenerateConsts.XML_TEMPLATE, CodeGenerateConsts.DAO);
        suffix.put(CodeGenerateConsts.MAPPER_TEMPLATE,CodeGenerateConsts.DAO);
        suffix.put(CodeGenerateConsts.ENTITY_TEMPLATE,CodeGenerateConsts.ENTITY);
        suffix.put(CodeGenerateConsts.CONTROLLER_TEMPLATE,CodeGenerateConsts.WEB);
        suffix.put(CodeGenerateConsts.FACADE_TEMPLATE,CodeGenerateConsts.FACADE);
        suffix.put(CodeGenerateConsts.FACADE_IMPL_TEMPLATE,CodeGenerateConsts.FACADE);
        suffix.put(CodeGenerateConsts.SERVICE_TEMPLATE,CodeGenerateConsts.SERVICE);
        suffix.put(CodeGenerateConsts.SERVICE_IMPL_TEMPLATE,CodeGenerateConsts.SERVICE);
        focEnv.setProjectSuffix(suffix);
        String target = Symbols.SLASH + targetPackage + Symbols.SLASH;
        focEnv.setTargetPath(target);
        focManager.prepare(focEnv);
        focManager.supply(focList);

        cfg.setFileOutConfigList(focList);
        autoGenerator.loadConfig(cfg);
    }



    /**
     * 包名配置
     */
    private void loadPackageConfig(){
        PackageConfig pc = new PackageConfig();
        pc.setParent(basePackage);
        String ctrlPack = "controller";
        String servicePack = "service";
        String serviceImplPack = "service.impl";
        String mapperPack = "dao";
        String entityPack = "entity";
        if(!StringUtil.isEmpty(targetPackage)){
            ctrlPack = ctrlPack + Symbols.FULL_STOP + targetPackage;
            servicePack = servicePack + Symbols.FULL_STOP + targetPackage;
            serviceImplPack = servicePack + Symbols.FULL_STOP + "impl";
            mapperPack = mapperPack + Symbols.FULL_STOP + targetPackage;
            entityPack = entityPack + Symbols.FULL_STOP + "po" + Symbols.FULL_STOP + targetPackage;
        }
        pc.setController(ctrlPack);
        pc.setService(servicePack);
        pc.setServiceImpl(serviceImplPack);
        pc.setMapper(mapperPack);
        pc.setEntity(entityPack);

        autoGenerator.loadConfig(pc);
    }


    /**
     * 策略配置
     */
    private void loadStrategyConfig(){
        StrategyConfig strategy = new StrategyConfig();
        // 忽略表结构前缀
        if(!StringUtil.isEmpty(ignorePrefix)){
            strategy.setTablePrefix(ignorePrefix);
        }
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setRestControllerStyle(true);
        strategy.setEntityLombokModel(true);
        strategy.setInclude(tables);
        strategy.setControllerMappingHyphenStyle(true);
        autoGenerator.loadConfig(strategy);
    }

    /**
     * 初始化操作
     * @param properties 配置内容
     * @throws IOException IO异常
     */
    protected Map<String,String> init(Map<String,String> properties) throws IOException {
        if(!MapUtil.isEmpty(properties)){
            initSwitch = true;
            return initProps(properties);
        }else {
            throw new RuntimeException(CodeErrMsg.CODE_GENERATOR_PROPS_ISSUE);
        }
    }

    /**
     * 初始化配置文件内容
     */
    private Map<String,String> initProps(Map<String,String> props){
        Map<String,String> map = new HashMap<>();
        this.url = props.get(CodeGenerateConsts.DS_URL);
        if(StringUtil.isEmpty(url)){
            throw new RuntimeException(CodeErrMsg.DB_URL_EMPTY);
        }
        this.driver = props.get(CodeGenerateConsts.DS_DRIVER);
        if(StringUtil.isEmpty(driver)){
            throw new RuntimeException(CodeErrMsg.DB_DRIVER_EMPTY);
        }
        username = props.get(CodeGenerateConsts.DS_USERNAME);
        password = props.get(CodeGenerateConsts.DS_PASSWORD);
        String tables = props.get(CodeGenerateConsts.DS_TABLES);
        if(StringUtil.isEmpty(tables)){
            throw new RuntimeException(CodeErrMsg.TABLES_EMPTY);
        }else {
            this.tables = tables.split(Symbols.COMMA);
        }
        this.author = props.getOrDefault(CodeGenerateConsts.AUTHOR,"");
        String overrideStr = props.getOrDefault(CodeGenerateConsts.OVERRIDE,"false");
        if(StateConst.FALSE.equals(overrideStr.trim().toLowerCase())){
            this.override = false;
        }else if(StateConst.TRUE.equals(overrideStr.trim().toLowerCase())){
            this.override = true;
        }else if(StringUtil.isBlank(overrideStr)){
            this.override = false;
        }else{
            throw new RuntimeException(CodeErrMsg.OVERRIDE_CONTENT_ERR);
        }
        this.projectName = props.get(CodeGenerateConsts.PROJECT_NAME);
        if(StringUtil.isEmpty(projectName)){
            throw new RuntimeException(CodeErrMsg.ARTIFACT_ID_EMPTY);
        }
        String projectPackage = props.get(CodeGenerateConsts.PROJECT_PACKAGE);
        if(StringUtil.isEmpty(projectPackage)){
            throw new RuntimeException(CodeErrMsg.PROJECT_PACK_EMPTY);
        }
        if(!projectPackage.contains(Symbols.FULL_STOP)){
            throw new RuntimeException(CodeErrMsg.PACK_NAME_WITHOUT_FULL_STOP);
        }
        String slashProjectPack = projectPackage.replace(Symbols.FULL_STOP,Symbols.SLASH);
        this.basePath = slashProjectPack;
        this.basePackage = projectPackage;
        map.put(CodeGenerateConsts.BASE_PACKAGE,this.basePackage);
        this.targetPackage = props.get(CodeGenerateConsts.TARGET_PACKAGE);
        if(StringUtil.isEmpty(targetPackage)){
            throw new RuntimeException(CodeErrMsg.TARGET_PACK_EMPTY);
        }

        this.ignorePrefix = props.get(CodeGenerateConsts.IGNORE_PREFIX);
        return map;
    }

    /**
     * 执行
     */
    protected void execute(Map<String,Object> params) throws IOException {
        if(!initSwitch){
           throw new RuntimeException(CodeErrMsg.INIT_METHOD_FIRST);
        }
        loadGlobalConfig();
        loadDataSource();
        loadPackageConfig();
        loadInjectConfig(params);
        loadStrategyConfig();
        autoGenerator.execute();
    }

}
