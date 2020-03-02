package com.hyiy.cummunity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = "com.hyiy.cummunity.mapper")
@MapperScan("com.hyiy.cummunity.mapper")
public class CommunityApplication {

    public static void main(String[] args ) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
