package xyz.shodown.code.consts;

/**
 * @ClassName: CodeErrMsg
 * @Description: 错误提示
 * @Author: wangxiang
 * @Date: 2021/6/18 16:29
 */
public interface CodeErrMsg {

    String DB_URL_EMPTY = "配置文件[code-generator.properties],数据库url未配置";

    String DB_DRIVER_EMPTY = "配置文件[code-generator.properties],数据库驱动未配置";

    String TABLES_EMPTY = "配置文件[code-generator.properties],配置项datasource.tables未指定";

    String ARTIFACT_ID_EMPTY = "配置文件[code-generator.properties],配置项artifact-id未指定";

    String PROJECT_PACK_EMPTY = "配置文件[code-generator.properties],配置项package-name未指定";

    String TARGET_PACK_EMPTY = "配置文件[code-generator.properties],配置项service-package未指定";

    String INIT_METHOD_FIRST = "配置文件[code-generator.properties],请先调用init方法进行初始化操作";

    String CODE_GENERATOR_PROPS_ISSUE = "配置文件[code-generator.properties],未配";

    String PACK_NAME_WITHOUT_FULL_STOP = "配置文件[code-generator.properties],package-name需要使用英文'.'作为分隔符";

    String OVERRIDE_CONTENT_ERR = "配置文件[code-generator.properties],override只能填写true或者false";
}
