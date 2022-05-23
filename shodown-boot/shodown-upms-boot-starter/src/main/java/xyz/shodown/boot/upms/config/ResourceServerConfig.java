package xyz.shodown.boot.upms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import xyz.shodown.common.util.basic.ListUtil;

import javax.annotation.Resource;

/**
 * @ClassName: ResourceServerConfig
 * @Description: 资源服务配置
 * @Author: wangxiang
 * @Date: 2021/9/5 20:12
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Resource
    private TokenStore tokenStore;

    @Resource
    private AdditionalProperties additionalProperties;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("res").tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.antMatcher("/resource/**").authorizeRequests();
        if(additionalProperties!=null&& !ListUtil.isEmpty(additionalProperties.getAccess().getIgnoreUrls())){
            // 增加不拦截的url
            for (String url : additionalProperties.getAccess().getIgnoreUrls()) {
                registry.antMatchers(url).permitAll();
            }
        }

        // 指定不同请求方式访问资源所需要的权限，一般查询是 read，其余是 write。
        registry.antMatchers(HttpMethod.GET, "/resource/**").access("#oauth2.hasScope('read')");
        registry.antMatchers(HttpMethod.POST, "/resource/**").access("#oauth2.hasScope('write')");
        registry.antMatchers(HttpMethod.PATCH, "/resource/**").access("#oauth2.hasScope('write')");
        registry.antMatchers(HttpMethod.PUT, "/resource/**").access("#oauth2.hasScope('write')");
        registry.antMatchers(HttpMethod.DELETE, "/resource/**").access("#oauth2.hasScope('write')");
        registry.and().headers().addHeaderWriter((request, response) -> {
            // 允许跨域
            response.addHeader("Access-Control-Allow-Origin", "*");
            // 如果是跨域的预检请求，则原封不动向下传达请求头信息
            if ("OPTIONS".equals(request.getMethod())) {
                response.setHeader("Access-Control-Allow-Methods",
                        request.getHeader("Access-Control-Request-Method"));
                response.setHeader("Access-Control-Allow-Headers",
                        request.getHeader("Access-Control-Request-Headers"));
            }
        });

    }
}
