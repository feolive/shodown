package xyz.shodown.common.util.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;
import xyz.shodown.common.util.basic.StringUtil;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @ClassName: ResourceUtil
 * @Description: 资源工具类
 * @Author: wangxiang
 * @Date: 2021/3/25 19:02
 */
public class ResourceUtil extends cn.hutool.core.io.resource.ResourceUtil {

    /**
     * 读取项目中properties配置文件内容
     * @param fileName 文件名称
     * @return map
     * @throws IOException IO异常
     */
    public static Map<String,String> readProperties(String fileName) throws IOException {
        Enumeration<URL> urls = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResources(fileName);
        Map<String,String> props = new HashMap<>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            UrlResource resource = new UrlResource(url);
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                String key = StringUtil.toStr(entry.getKey()).trim();
                String value = StringUtil.toStr(entry.getValue()).trim();
                props.put(key,value);
            }
        }
        return props;
    }


    public static String readJarFile(Class<?> clazz) throws IOException {
        String exePath = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        JarFile localJarFile = new JarFile(new File(exePath));
        StringBuilder sb = new StringBuilder();
        Enumeration<JarEntry> entries = localJarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            System.out.println(jarEntry.getName());
            String innerPath = jarEntry.getName();
            if(innerPath.startsWith("conf")){
                InputStream inputStream = clazz.getClassLoader().getResourceAsStream(innerPath);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line="";
                while((line=br.readLine())!=null){
                    sb.append(line);
                    System.out.println(innerPath+"内容为:"+line);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 读取项目中json配置文件内容
     * @param filePath 文件路径
     * @return map
     * @throws Exception IO异常
     */
    public static  Map<String, Object>readJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map map = objectMapper.readValue(new File(filePath), Map.class);
            return map ;
        } catch (Exception e) {
      e.printStackTrace();
            return null ;
        }
    }

    /**
     * 读取yaml配置文件内容
     * @param filePath 文件路径
     * @return map
     * @throws Exception IO异常
     */
    public static Map<String, Object> readYaml(String filePath) throws FileNotFoundException {

        InputStream input = new FileInputStream(new File(filePath));
        Yaml yaml = new Yaml();
        Map<String, Object> object = (Map<String, Object>) yaml.load(input);
        return object;
    }



}
