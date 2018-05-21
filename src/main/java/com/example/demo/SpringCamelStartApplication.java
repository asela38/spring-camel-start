package com.example.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringCamelStartApplication extends RouteBuilder implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringCamelStartApplication.class, args);
    }

    @Autowired
    private CamelContext camelContext;

    @Override
    public void run(ApplicationArguments arg0) throws Exception {

        camelContext.getShutdownStrategy().setTimeout(20L);
        camelContext.start();
        Thread.currentThread().join();
    }

    @Override
    public void configure() throws Exception {
        from("timer://foo?fixedRate=true&period=1000")
            .setBody(simple("SELECT * FROM user_test ORDER BY user_id DESC LIMIT 1;"))
            .log("JDBC Query : ${body}")
            .to("jdbc:dataSource?useHeadersAsParameters=true")
            .log("JDBC Result : ${body}");
    }
}
