package com.example.application.config;

import com.example.application.service.FileProcessingService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

    @Value("${file.watch.directory}")
    private String watchDirectory;

    @Bean
    public RouteBuilder fileWatcherRoute(FileProcessingService fileProcessingService) {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("file://" + watchDirectory + "?noop=true&include=.*\\.xml")
                        .routeId("file-watcher")
                        .log("Обнаружен файл: ${header.CamelFileName}")
                        .process(exchange -> {
                            String filename = exchange.getIn().getHeader("CamelFileName", String.class);
                            byte[] body = exchange.getIn().getBody(byte[].class);
                            // вызов вашего сервиса
                            fileProcessingService.processFile(body, filename);
                        });
            }
        };
    }
}
