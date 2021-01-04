package com.winbaoxian.testng.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author dongxuanliang252
 * @date 2020-7-3 14:00:33
 */
@SpringBootApplication(scanBasePackages = {"com.winbaoxian.testng"}, exclude = {MongoAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"com.winbaoxian.testng.repository"})
@EntityScan(basePackages = {"com.winbaoxian.testng.model.entity"})
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
