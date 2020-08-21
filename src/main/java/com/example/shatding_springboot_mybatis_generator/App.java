package com.example.shatding_springboot_mybatis_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * 
 *  说明：加入swagger api文档接口
 *  @author:heshengjin qq:2356899074
 *  @date 2020年7月7日 下午3:06:02
 */
@SpringBootApplication
@EnableSwagger2
public class App {
    public static void main( String[] args ){
    	SpringApplication.run(App.class, args);
    }
}
