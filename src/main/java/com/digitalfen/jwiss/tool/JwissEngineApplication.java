package com.digitalfen.jwiss.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

import com.digitalfen.jwiss.devkit.handlers.JwissLogger;
import com.digitalfen.jwiss.tool.boot.JwissAddonLoader;
import com.digitalfen.jwiss.tool.boot.JwissConfigLoader;
import com.digitalfen.jwiss.tool.boot.JwissPluginLoader;

@SpringBootApplication(exclude = { MultipartAutoConfiguration.class,
	JmxAutoConfiguration.class, })
public class JwissEngineApplication {

    /**
     * Jwiss engine loader
     * 
     * @param String[] args
     */
    public static void main(String[] args) {

	JwissLogger.printer.global("Starting Jwiss Engine...");
	SpringApplication.run(JwissEngineApplication.class, args);

	JwissLogger.printer.global("Loading Jwiss Configurations...");
	JwissConfigLoader configLoader = new JwissConfigLoader();
	configLoader.init();
	JwissLogger.printer.global("Loading Jwiss Configurations Done.");

	JwissLogger.printer.global("Loading Jwiss Addons...");
	JwissAddonLoader addonLoader = new JwissAddonLoader();
	addonLoader.init();
	JwissLogger.printer.global("Loading Jwiss Addons Done.");

	JwissLogger.printer.global("Loading Jwiss Plugins...");
	JwissPluginLoader pluginLoader = new JwissPluginLoader();
	pluginLoader.init();
	JwissLogger.printer.info("Loading Jwiss Plugins Done.");

	JwissLogger.printer.global("Jwiss Engine started.");
	
	

    }

}
