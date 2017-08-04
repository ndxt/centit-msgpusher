package com.centit.msgpusher.config;

import com.centit.framework.hibernate.config.DataSourceConfig;
import com.centit.framework.staticsystem.config.StaticSystemBeanConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = {"com.centit.*"})
@Import({StaticSystemBeanConfig.class, DataSourceConfig.class})
public class ServiceConfig {

}
