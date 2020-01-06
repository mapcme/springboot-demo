package com.mapc.annotation;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @desc: 年级Grade转换器
 * @author: duchao
 * @date: 2020/01/06 15:07
 */
@Component
@ConfigurationPropertiesBinding
public class GradeConverter implements Converter<String,Grade> {

    @Override
    public Grade convert(String source) {
        Grade grade = new Grade();
        grade.setName(source);
        return grade;
    }
}
