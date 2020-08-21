package com.example.shatding_springboot_mybatis_generator.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 腾云接口数据返回封装
 *
 * @author Dylan
 * @since 2020-06-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    private Integer code;

    private String desc;

    private Object data;

    public static Result error() {
    	return Result.builder().code(1).desc("失败").build();
    }
    public static Result error(int code) {
    	return Result.builder().code(code).desc("失败").build();
    }
    public static Result error(String desc) {
        return Result.builder().code(1).desc(desc).build();
    }
    public static Result error(int code, String desc) {
        return Result.builder().code(code).desc(desc).build();
    }
    
    public static Result success() {
        return  Result.builder().code(0).desc("成功").build();
    }
    public static Result success(Object data) {
        return Result.builder().code(0).desc("成功").data(data).build();
    }
    public static Result success(String desc) {
        return Result.builder().code(0).desc(desc).build();
    } 
    public static Result success(Object data, String desc) {
        return Result.builder().code(0).desc(desc).data(data).build();
    }
}
