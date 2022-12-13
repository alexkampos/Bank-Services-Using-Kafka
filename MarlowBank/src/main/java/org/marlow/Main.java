package org.marlow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EntityScan(basePackages = {"org.marlow.model"})
@EnableJpaRepositories(basePackages = "org.marlow.repository")
@EnableAsync
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

    }

}