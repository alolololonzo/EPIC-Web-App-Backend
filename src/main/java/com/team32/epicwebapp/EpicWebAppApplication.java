package com.team32.epicwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan
@SpringBootApplication
public class EpicWebAppApplication{

    public static boolean connected = false;

    public static void main(String[] args) throws Throwable {
        SSHConnection sshConnection = new SSHConnection();
        connected = true;
        SpringApplication.run(EpicWebAppApplication.class, args);

    }

}
