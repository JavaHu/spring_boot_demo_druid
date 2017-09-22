package com.springboot.demo.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hu.
 * @date 2017/9/22
 * @time 9:30
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidProperties {
}
