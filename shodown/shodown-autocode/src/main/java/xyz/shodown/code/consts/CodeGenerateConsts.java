package xyz.shodown.code.consts;

/**
 * @ClassName: CodeGenerateConsts
 * @Description:
 * @Author: wangxiang
 * @Date: 2021/3/26 11:30
 */
public interface CodeGenerateConsts {

    /**
     * .vm
     */
    String DOT_VM = ".vm";

    /**
     * facadeName
     */
    String FACADE_NAME = "facadeName";

    /**
     * facadeImplName
     */
    String FACADE_IMPL_NAME = "facadeImplName";

    /**
     * PROP_LOCATION
     */
    String PROP_LOCATION = "code-generator.properties";

    /**
     * packFacadeImpl
     */
    String PACK_FACADE_IMPL = "packFacadeImpl";

    /**
     * packFacade
     */
    String PACK_FACADE = "packFacade";

    /**
     * basePackage
     */
    String BASE_PACKAGE = "basePackage";

    /**
     * targetPackage
     */
    String TARGET_PACK = "targetPackage";

    // ---------------------------------------

    String JAR = "jar";

    /**
     * java代码地址
     */
    String SRC_JAVA = "/src/main/java/";

    /**
     * resource地址
     */
    String SRC_RESOURCE = "/src/main/resources/mapper";

    /**
     * 项目路径
     */
    String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * dao
     */
    String DAO = "dao";

    /**
     * entity
     */
    String ENTITY = "entity";

    /**
     * controller
     */
    String CONTROLLER = "controller";

    /**
     * service
     */
    String SERVICE = "service";

    /**
     * facade
     */
    String FACADE = "facade";

    /**
     * web
     */
    String WEB = "web";

    String XML_TEMPLATE = "/templates/mapper.xml.vm";

    String MAPPER_TEMPLATE = "/templates/mapper.java.vm";

    String ENTITY_TEMPLATE = "/templates/entity.java.vm";

    String CONTROLLER_TEMPLATE = "/templates/controller.java.vm";

    String FACADE_TEMPLATE = "/templates/facade.java.vm";

    String FACADE_IMPL_TEMPLATE = "/templates/facadeImpl.java.vm";

    String SERVICE_TEMPLATE = "/templates/service.java.vm";

    String SERVICE_IMPL_TEMPLATE = "/templates/serviceImpl.java.vm";

    // ---------------------配置文件内容------------------
    String OUTPUT_DIR = "output-dir";

    String AUTHOR = "author";

    String OVERRIDE = "override";

    String DS_URL = "datasource.url";

    String DS_DRIVER = "datasource.driver";

    String DS_USERNAME = "datasource.username";

    String DS_PASSWORD = "datasource.password";

    String DS_TABLES = "datasource.tables";

    String IGNORE_PREFIX = "ignore-prefix";

    String PROJECT_NAME = "artifact-id";

    String PROJECT_PACKAGE = "package-name";

    String TARGET_PACKAGE = "service-package";

    String VM_LOAD_CLASS = "resource.loader.jar.class";

    String VM_LOAD_CLASS_VALUE = "org.apache.velocity.runtime.resource.loader.JarResourceLoader";

    String VM_LOAD_PATH = "resource.loader.jar.path";

    String VM_LOAD_UNICODE = "resource.loader.file.unicode";

}
