package com.mapc.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationPropertiesScan
//@EnableConfigurationProperties(StudentProperties.class)
@SpringBootApplication
public class ConfigurationPropertiesApplication implements ApplicationRunner {

    //@Autowired
    //private StudentProperties studentProperties;

    @Autowired
    private Teacher teacher;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //System.out.println(studentProperties.toString());
        System.out.println(teacher.toString());
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigurationPropertiesApplication.class, args);
    }
}
