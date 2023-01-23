package com.onbelay.dagnabitapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.onbelay.*"})
@EntityScan(basePackages = {"com.onbelay.*"})
@SpringBootApplication
public class DagnabitApplication {

    public static void main(String[] args) {

        new SpringApplicationBuilder(DagnabitApplication.class)
                .run(args);
    }

}
