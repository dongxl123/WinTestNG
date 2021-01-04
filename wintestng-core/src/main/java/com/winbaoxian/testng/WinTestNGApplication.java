package com.winbaoxian.testng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 10:48
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class WinTestNGApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(WinTestNGApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WinTestNGApplication.class, args);
    }

}
