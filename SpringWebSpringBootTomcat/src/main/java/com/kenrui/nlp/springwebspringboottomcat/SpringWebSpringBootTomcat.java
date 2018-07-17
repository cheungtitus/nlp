package com.kenrui.nlp.springwebspringboottomcat;

import com.kenrui.nlp.nlpcore.utilities.SystemInfo;
import com.kenrui.nlp.springwebspringboottomcat.config.AppConfig;
import com.kenrui.nlp.springwebspringboottomcat.models.ConnectionInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pcap4j.core.PcapNativeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SpringWebSpringBootTomcat {
    private static final Logger logger = LogManager.getLogger("NLP");

    @Autowired private ConnectionInfo localServerEndpoint;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        SpringWebSpringBootTomcat springWebSpringBootTomcat = context.getBean(SpringWebSpringBootTomcat.class);

        logger.info("Started SpringWebSpringBootTomcat listening on " + springWebSpringBootTomcat.localServerEndpoint.getPort());

        SystemInfo.printAvailableCores();
        SystemInfo.printMemory();
        try {
            SystemInfo.getInterfacesDetail();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }

        SpringApplication.run(SpringWebSpringBootTomcat.class, args);
    }
}