package cn.begonia.sso.ssp.begonia_ssp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
@EnableScheduling
@SpringBootApplication
public class BegoniaSspApplication {

    public static void main(String[] args) {
        SpringApplication.run(BegoniaSspApplication.class, args);
    }

}
