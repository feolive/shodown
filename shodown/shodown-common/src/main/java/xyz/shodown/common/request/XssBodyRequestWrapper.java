package xyz.shodown.common.request;

import lombok.extern.slf4j.Slf4j;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.util.io.XssUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @ClassName: XssBodyRequestWrapper
 * @Description: 处理application/json格式请求
 * @Author: wangxiang
 * @Date: 2021/5/24 20:44
 */
@Slf4j(topic = LogCategory.PLATFORM)
public class XssBodyRequestWrapper extends HttpServletRequestWrapper {

    private String body;

    private String errMsg;

    public XssBodyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = request.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
            String tmp = stringBuilder.toString();
            if (XssUtil.cleanXssAndSqlIllegals(tmp)) {
                errMsg = request.getRequestURI() + "请求的body中包含非法内容;";
                log.error(this.errMsg);
            }
            body = tmp;
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getBody() {
        return body;
    }

}
