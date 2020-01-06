package com.mapc.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @desc: 年级
 * @author: duchao
 * @date: 2020/01/06 13:54
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    private String name;
    private String school;

}
