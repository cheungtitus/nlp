package com.kenrui.nlp.taxonomy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfigTaxonomyModelWrapper {
    @Bean
    public String pythonLibPath() {
        return "C:\\Users\\cheun\\IdeaProjects\\nlp\\taxonomy\\src\\main\\resources\\Lib";
    }
}
