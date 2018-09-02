package com.kenrui.nlp.springwebfluxspringbootnetty;

import com.kenrui.nlp.common.SystemInfo;
import com.kenrui.nlp.springwebfluxspringbootnetty.config.AppConfig;
import com.kenrui.nlp.common.ConnectionInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pcap4j.core.PcapNativeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SpringWebFluxSpringBootNetty {
    private static final Logger logger = LogManager.getLogger("NLP");

    @Autowired private ConnectionInfo localServerEndpoint;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        SpringWebFluxSpringBootNetty springWebFluxSpringBootNetty = context.getBean(SpringWebFluxSpringBootNetty.class);

        logger.info("Started SpringWebFluxSpringBootNetty listening on " + springWebFluxSpringBootNetty.localServerEndpoint.getPort());

        SystemInfo.printAvailableCores();
        SystemInfo.printMemory();
        try {
            SystemInfo.getInterfacesDetail();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }

        SpringApplication.run(SpringWebFluxSpringBootNetty.class, args);
    }
}