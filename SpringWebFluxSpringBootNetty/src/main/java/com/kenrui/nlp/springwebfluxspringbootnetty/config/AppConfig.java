package com.kenrui.nlp.springwebfluxspringbootnetty.config;

import com.kenrui.nlp.nlpcore.NLPControllerMVC;
import com.kenrui.nlp.nlpcore.NLPControllerReactive;
import com.kenrui.nlp.springwebfluxspringbootnetty.models.ConnectionInfo;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.io.File;

@Configuration
@ComponentScan(value = "com.kenrui.nlp",
        includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = NLPControllerReactive.class),
        excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = NLPControllerMVC.class))
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
