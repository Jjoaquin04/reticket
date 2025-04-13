package com.example.reticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ReticketApplication {
        public static void main(String[] args) {
                // Load environment variables from .env
                Dotenv dotenv = Dotenv.configure().load();
                System.setProperty("MYSQL_URL_JDBC", dotenv.get("MYSQL_URL_JDBC"));
                System.setProperty("MYSQL_USER", dotenv.get("MYSQL_USER"));
                System.setProperty("MYSQL_PASS", dotenv.get("MYSQL_PASS"));
                
                SpringApplication.run(ReticketApplication.class, args);
        }
}
