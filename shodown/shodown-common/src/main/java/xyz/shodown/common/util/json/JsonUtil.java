package xyz.shodown.common.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: JsonUtil
 * @Description: JSON工具类
 * @Author: wangxiang
 * @Date: 2021/2/4 15:41
 */
public final class JsonUtil {

    private final static Gson GSON;

    static {
            GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
            GSON = gsonBuilder.create();
    }

    /**
     * 对象转成json字符串
     * @param object 对象
     * @return json字符串
     */
    public static String objectToJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * Json转成对象
     * @param json json字符串
     * @param cls 对象类
     * @param <T> 泛型
     * @return 对象
     */
    public static <T> T jsonToBean(String json, Class<T> cls) {
        return GSON.fromJson(json, cls);
    }

    /**
     * json转成list<T>
     * @param json json字符串
     * @param cls 对象类
     * @param <T> 泛型
     * @return 对象list
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        return GSON.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * json转成List<Map<String,T>>
     * @param json json字符串
     * @param <T> 泛型
     * @return List<Map<String,T>>
     */
    public static <T> List<Map<String, T>> jsonToListMaps(String json) {
        return GSON.fromJson(json, new TypeToken<List<Map<String, T>>>() {
        }.getType());
    }

    /**
     * json转成map
     * @param json json字符串
     * @param <T> 泛型
     * @return map
     */
    public static <T> Map<String, T> jsonToMap(String json) {
        return GSON.fromJson(json, new TypeToken<Map<String, T>>() {
        }.getType());
    }

}
