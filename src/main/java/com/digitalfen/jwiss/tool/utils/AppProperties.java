package com.digitalfen.jwiss.tool.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Component
@Getter
@NoArgsConstructor
public class AppProperties {

    @Value("${spring.application.name}")
    private String name;

    @Value("${spring.application.version}")
    private String version;

    @Value("${spring.application.description}")
    private String description;

    @Value("${spring.application.java}")
    private String java;
}
