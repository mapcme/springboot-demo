package com.mapc.annotation;

import com.sun.istack.internal.NotNull;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.DurationFormat;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @desc: @ConfigurationProperties
 * @author: duchao
 * @date: 2020/01/06 13:49
 */
//@Data
@ToString
@Validated
//@Configuration
@ConfigurationProperties(prefix = "student",ignoreInvalidFields = false,ignoreUnknownFields = false)
public class StudentProperties {

    @ConstructorBinding
    public StudentProperties(String name, int age, Grade grade, School school, List<String> hobbyList, Duration allowLateTime) {
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.school = school;
        this.hobbyList = hobbyList;
        this.allowLateTime = allowLateTime;
    }

    /**
     * 姓名
     */
    @NotEmpty(message = "学生姓名不能为空")
    private String name;

    /**
     * 年龄
     */
    private int age;

    /**
     * 年级
     */
    private Grade grade;

    /**
     * 学校
     */
    private School school;

    /**
     * 爱好
     */
    private List<String> hobbyList;

    /**
     * 允许迟到时间
     */
    @DurationUnit(ChronoUnit.MINUTES)
    @DurationFormat(DurationStyle.SIMPLE)
    private Duration allowLateTime;

    /**
     * 性别
     */
    //@Deprecated
    //private String sex="男";

//    @DeprecatedConfigurationProperty(reason = "属性弃用",replacement = "none")
//    public String getSex() {
//        return sex;
//    }
}
