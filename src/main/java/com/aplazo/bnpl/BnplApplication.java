package com.aplazo.bnpl;

import com.aplazo.bnpl.service.ClientService;
import com.aplazo.bnpl.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BnplApplication {


    public static void main(String[] args) {
        SpringApplication.run(BnplApplication.class, args);
    }

}
