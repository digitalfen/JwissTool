package com.digitalfen.jwiss.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

import com.digitalfen.jwiss.tool.boot.JwissEngineLoader;

@SpringBootApplication(exclude = { MultipartAutoConfiguration.class,
	JmxAutoConfiguration.class, })
public class JwissEngineApplication {

    /**
     * JwissTool engine loader
     * 
     * @param String[] args
     */
    public static void main(String[] args) {

	SpringApplication.run(JwissEngineApplication.class, args);

	JwissEngineLoader engineLoader = new JwissEngineLoader();
	engineLoader.setArgs(args);
	engineLoader.init();

    }

}
