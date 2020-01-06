package com.mapc.annotation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @desc: 教师Config配置
 * @author: duchao
 * @date: 2020/01/06 16:43
 */
@Configuration
public class TeacherConfig {
    @Bean
    @ConfigurationProperties(prefix = "teacher")
    public Teacher teacher1(){
        return new Teacher();
    }
}
