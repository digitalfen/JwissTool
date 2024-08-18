package com.digitalfen.jwiss.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

import com.digitalfen.jwiss.devkit.handlers.JwissCache;
import com.digitalfen.jwiss.tool.boot.JwissEngineLoader;
import com.digitalfen.jwiss.tool.service.JwissActionService;
import com.digitalfen.jwiss.tool.service.JwissCommandService;

@SpringBootApplication(exclude = { MultipartAutoConfiguration.class,
	JmxAutoConfiguration.class, })
public class JwissEngineApplication {

    /**
     * JwissTool Engine
     * 
     * @param String[] args
     */
    public static void main(String[] args) {

	SpringApplication.run(JwissEngineApplication.class, args);

	JwissEngineLoader engineLoader = new JwissEngineLoader();
	engineLoader.setArgs(args);
	engineLoader.init();

	JwissActionService actionService = new JwissActionService();
	actionService.setInput(args);
	actionService.run();

	JwissCommandService commandService = new JwissCommandService();
	commandService.setInput(JwissCache.configurations.get("starting-commands"));
	commandService.run();

    }

}
