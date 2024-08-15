package com.digitalfen.jwiss.tool.boot;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import com.digitalfen.jwiss.devkit.annotations.JwissPluginMetadata;
import com.digitalfen.jwiss.devkit.handlers.JwissCache;
import com.digitalfen.jwiss.devkit.handlers.JwissLogger;
import com.digitalfen.jwiss.devkit.interfaces.JwissPluginInterface;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwissPluginLoader {

    public void init() {

	try {
	    JwissLogger.printer.info("Checking Installed Plugins...");

	    loadPlugins();

	    JwissLogger.printer.info("Checking Installed Plugins Done.");

	} catch (Exception e) {

	    JwissLogger.printer.error("An error was stopped the plugin installation.");
	    JwissLogger.printer.error(e);

	}

    }

    public void loadPlugins() throws Exception {
	File pluginsDir = new File("plugins");

	JwissLogger.printer.info("Checking for Plugins JAR files...");

	File[] jarFiles = pluginsDir.listFiles((dir, name) -> name.endsWith(".jar"));

	JwissLogger.printer.debug(jarFiles.length + " JAR were found.");

	List<URL> jarUrls = new ArrayList<>();
	if (jarFiles != null) {
	    for (File jarFile : jarFiles) {
		jarUrls.add(jarFile.toURI().toURL());
		JwissLogger.printer.debug(
			"> " + jarFile.toURI().toURL().toString().split("file:/")[1]);
	    }
	}
	JwissLogger.printer.info("Checking for Plugins JAR files Done.");

	JwissLogger.printer.info("Getting Class reference from JAR files...");

	URLClassLoader urlClassLoader = new URLClassLoader(jarUrls.toArray(new URL[0]),
		JwissPluginLoader.class.getClassLoader());

	Reflections reflections = new Reflections(new ConfigurationBuilder()
		.setUrls(jarUrls)
		.addClassLoaders(urlClassLoader)
		.setScanners(new SubTypesScanner(), new TypeAnnotationsScanner()));

	JwissLogger.printer.debug("Reflections instance created");

	Set<Class<? extends JwissPluginInterface>> pluginClasses = reflections
		.getSubTypesOf(JwissPluginInterface.class);

	JwissLogger.printer
		.debug("Found " + pluginClasses.size() + " plugin class references.");

	for (Class<? extends JwissPluginInterface> pluginClass : pluginClasses) {

	    if (pluginClass.isAnnotationPresent(JwissPluginMetadata.class)) {

		JwissPluginMetadata pluginCommand = pluginClass
			.getAnnotation(JwissPluginMetadata.class);

		JwissLogger.printer.debug("Found metadata for " + pluginClass);

		JwissPluginInterface pluginInstance = pluginClass.getDeclaredConstructor()
			.newInstance();

		JwissLogger.printer.debug("Plugin instance created for " + pluginClass);

		JwissLogger.printer
			.debug("Plugin " + pluginClass + " was loaded with success.");

		JwissLogger.printer.debug("Caching instance of " + pluginClass);

		JwissCache.plugins.put(pluginCommand.usage(), pluginInstance);

		JwissLogger.printer
			.debug("Instance of " + pluginClass + " cached with success");

	    }
	}
	JwissLogger.printer.info("Getting Class reference from JAR files Done.");

    }

}
