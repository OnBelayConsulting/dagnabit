package com.onbelay.testapp;

import com.onbelay.testapp.config.ApplicationShutdown;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.onbelay.*"})
@EntityScan(basePackages = {"com.onbelay.*"})
@SpringBootApplication
public class DagnabitTestApplication {


    public static void main(String[] args) {

        new SpringApplicationBuilder(DagnabitTestApplication.class)
                .listeners(new ApplicationShutdown())
                .run(args);

    }
}