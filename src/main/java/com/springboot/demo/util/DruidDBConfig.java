package com.springboot.demo.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author hu.
 * @date 2017/9/21
 * @time 16:26
 * EnvironmentAware   重写方法 setEnvironment 可以在工程启动时，获取到系统环境变量和application配置文件中的变量。
 */
@Configuration
public class DruidDBConfig implements EnvironmentAware {

    private Logger logger = LoggerFactory.getLogger(DruidDBConfig.class);
    private RelaxedPropertyResolver propertyResolver;//用于读取具体的属性
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
    }


    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        registrationBean.addInitParameter("allow", "127.0.0.1");//白名单
        registrationBean.addInitParameter("deny", "192.168.31.234");//黑名单
        //http://localhost:8080/druid/index.html 访问druid，以下为用户名和密码
        registrationBean.addInitParameter("loginUsername", "admin");
        registrationBean.addInitParameter("loginPassword", "111111");
        registrationBean.addInitParameter("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean druidWebStatViewFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new WebStatFilter());
        registrationBean.addInitParameter("urlPatterns", "/*");
        registrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");//忽略资源

        return registrationBean;

    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DruidDataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        try {
            if (StringUtils.isEmpty(this.propertyResolver.getProperty("url"))) {
                System.out.println("Your database connection pool configuration is incorrect! Please check your Spring profile, current profiles are:" + Arrays.toString(this.environment.getActiveProfiles()));

                throw new ApplicationContextException("Database connection pool is not configured correctly");
            }
            druidDataSource.setDriverClassName(this.propertyResolver.getProperty("driver-class-name"));
            druidDataSource.setUrl(this.propertyResolver.getProperty("url"));
            druidDataSource.setUsername(this.propertyResolver.getProperty("username"));
            druidDataSource.setPassword(CipherUtil.decode(this.propertyResolver.getProperty("password")));
            druidDataSource.setInitialSize(Integer.parseInt(this.propertyResolver.getProperty("initialSize")));
            druidDataSource.setMinIdle(Integer.parseInt(this.propertyResolver.getProperty("minIdle")));
            druidDataSource.setMaxActive(Integer.parseInt(this.propertyResolver.getProperty("maxActive")));
            druidDataSource.setMaxWait(Integer.parseInt(this.propertyResolver.getProperty("maxWait")));
            druidDataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(this.propertyResolver.getProperty("timeBetweenEvictionRunsMillis")));
            druidDataSource.setMinEvictableIdleTimeMillis(Long.parseLong(this.propertyResolver.getProperty("minEvictableIdleTimeMillis")));
            druidDataSource.setValidationQuery(this.propertyResolver.getProperty("validationQuery"));
            druidDataSource.setTestWhileIdle(Boolean.parseBoolean(this.propertyResolver.getProperty("testWhileIdle")));
            druidDataSource.setTestOnBorrow(Boolean.parseBoolean(this.propertyResolver.getProperty("testOnBorrow")));
            druidDataSource.setTestOnReturn(Boolean.parseBoolean(this.propertyResolver.getProperty("testOnReturn")));
            druidDataSource.setPoolPreparedStatements(Boolean.parseBoolean(this.propertyResolver.getProperty("poolPreparedStatements")));
            druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(this.propertyResolver.getProperty("maxPoolPreparedStatementPerConnectionSize")));

            druidDataSource.setFilters(this.propertyResolver.getProperty("filters"));

            logger.debug("Druid数据源设置成功......" + druidDataSource.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug("Druid数据源设置失败......", e);
        }
        return druidDataSource;
    }
}
