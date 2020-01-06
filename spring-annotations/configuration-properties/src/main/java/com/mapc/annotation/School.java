package com.mapc.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc: 学校
 * @author: duchao
 * @date: 2020/01/06 15:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class School {
    private String name;
    private String locate;
}
