package xyz.shodown.common.request;

import lombok.extern.slf4j.Slf4j;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.util.io.XssUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * 防止sql注入,xss攻击
 * 前端可以对输入信息做预处理，后端也可以做处理。
 */
@Slf4j(topic = LogCategory.PLATFORM)
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String errMsg;
    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
        errMsg = servletRequest.getRequestURI() + " 请求包含敏感字符与非法内容";
    }

    /**覆盖getParameter方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        if(XssUtil.cleanXssAndSqlIllegals(value)){
            log.error(this.errMsg);
        }
        return value;
    }
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        boolean flag = false;
        for (int i = 0; i < count; i++) {
            if(XssUtil.cleanXssAndSqlIllegals(values[i])){
                flag = true;
            }
            encodedValues[i] = values[i];
        }
        if(flag){
            log.error(this.errMsg);
        }
        return encodedValues;
    }
    @Override
    public Map<String, String[]> getParameterMap(){
        Map<String, String[]> values=super.getParameterMap();
        if (values == null) {
            return null;
        }
        Map<String, String[]> result=new HashMap<>();
        boolean flag = false;
        for(String key:values.keySet()){
            String encodedKey = new String(key);
            if(XssUtil.cleanXssAndSqlIllegals(encodedKey)){
                log.error(this.errMsg);
                flag = true;
            }

            int count=values.get(key).length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++){
                if(!flag&&XssUtil.cleanXssAndSqlIllegals(values.get(key)[i])){
                    log.error(this.errMsg);
                    flag = true;
                }
                encodedValues[i]=values.get(key)[i];
            }
            result.put(encodedKey,encodedValues);
        }
        return result;
    }
    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        if(XssUtil.cleanXssAndSqlIllegals(value)){
            log.error(this.errMsg);
        }
        return value;
    }
 

}
