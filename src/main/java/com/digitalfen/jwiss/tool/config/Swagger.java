package com.digitalfen.jwiss.tool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.digitalfen.jwiss.tool.utils.AppProperties;
import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger {

    private final TypeResolver typeResolver;
    private AppProperties props;

    public Swagger(AppProperties props, final TypeResolver typeResolver) {
	this.props = props;
	this.typeResolver = typeResolver;

    }

    @Bean
    public Docket api() {
	return new Docket(DocumentationType.SWAGGER_2).select()
		.apis(RequestHandlerSelectors.basePackage("com.digitalfen.jwiss.engine"))
		.build()
		.apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
	return new ApiInfoBuilder().title(props.getName())
		.description(props.getDescription())
		.version(props.getVersion()).build();
    }
}
