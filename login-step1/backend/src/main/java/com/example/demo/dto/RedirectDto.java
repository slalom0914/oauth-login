package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedirectDto {
    private String code;

    @SpringBootApplication
    public static class BackendApplication {

        public static void main(String[] args) {
            SpringApplication.run(BackendApplication.class, args);
        }

    }
}
