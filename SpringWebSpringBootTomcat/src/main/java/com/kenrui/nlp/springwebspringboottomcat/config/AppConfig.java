package com.kenrui.nlp.springwebspringboottomcat.config;

import com.kenrui.nlp.common.ConnectionInfo;
import com.kenrui.nlp.common.config.JPAConfig;
import com.kenrui.nlp.nlpcore.NLPControllerReactive;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;

import java.io.File;

@Configuration
@Import(JPAConfig.class)
@ComponentScan(basePackages = "com.kenrui.nlp",
        excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = NLPControllerReactive.class))
public class AppConfig {
    Config defaultConfig = ConfigFactory.parseFile(new File("configs/defaults.conf"));

    @Bean
    public ConnectionInfo localServerEndpoint() {
        return new ConnectionInfo(defaultConfig.getString("localServer.ip"),
                defaultConfig.getInt("localServer.port"),
                defaultConfig.getString("localServer.hostname"),
                defaultConfig.getString("localServer.description"));
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setPort(this.localServerEndpoint().getPort());
//        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notfound.html"));
        return factory;
    }
}
