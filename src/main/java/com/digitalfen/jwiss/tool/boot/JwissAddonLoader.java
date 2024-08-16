package com.digitalfen.jwiss.tool.boot;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import com.digitalfen.jwiss.devkit.annotations.JwissAddonMetadata;
import com.digitalfen.jwiss.devkit.handlers.JwissCache;
import com.digitalfen.jwiss.devkit.handlers.JwissLogger;
import com.digitalfen.jwiss.devkit.interfaces.JwissAddonInterface;
import com.digitalfen.jwiss.devkit.interfaces.JwissLoaderInterface;

import lombok.NoArgsConstructor;

/**
 * Load and cache instaled addons in startup.
 * 
 */
@NoArgsConstructor
public class JwissAddonLoader implements JwissLoaderInterface {

    /**
     * JwissLoaderInterface
     * 
     * @return void
     */
    @Override
    public void init() {
	try {
	    JwissLogger.printer.info("Checking Installed Addons...");

	    loadAddons();

	    JwissLogger.printer.info("Checking Installed Addons Done.");

	} catch (Exception e) {
	    JwissLogger.printer.error("An error was stopped the addon installation.");
	    JwissLogger.printer.error(e);
	}
    }

    /**
     * Load and cache addons
     * 
     * @return void
     */
    public void loadAddons() throws Exception {
	File addonsDir = new File("addons");

	JwissLogger.printer.info("Checking for Addons JAR files...");

	File[] jarFiles = addonsDir.listFiles((dir, name) -> name.endsWith(".jar"));

	JwissLogger.printer.debug(jarFiles.length + " JAR were found.");

	List<URL> jarUrls = new ArrayList<>();
	if (jarFiles != null) {
	    for (File jarFile : jarFiles) {
		jarUrls.add(jarFile.toURI().toURL());
		JwissLogger.printer.debug(
			"> " + jarFile.toURI().toURL().toString().split("file:/")[1]);
	    }
	}
	JwissLogger.printer.info("Checking for Addons JAR files Done.");

	JwissLogger.printer.info("Getting Class reference from JAR files...");

	URLClassLoader urlClassLoader = new URLClassLoader(jarUrls.toArray(new URL[0]),
		JwissAddonLoader.class.getClassLoader());

	Reflections reflections = new Reflections(new ConfigurationBuilder()
		.setUrls(jarUrls)
		.addClassLoaders(urlClassLoader)
		.setScanners(new SubTypesScanner(), new TypeAnnotationsScanner()));

	JwissLogger.printer.debug("Reflections instance created");

	Set<Class<? extends JwissAddonInterface>> addonClasses = reflections
		.getSubTypesOf(JwissAddonInterface.class);

	for (Class<? extends JwissAddonInterface> addonClass : addonClasses) {
	    if (addonClass.isAnnotationPresent(JwissAddonMetadata.class)) {

		JwissAddonMetadata addonMetadata = addonClass
			.getAnnotation(JwissAddonMetadata.class);

		JwissLogger.printer.debug("Found metadata for " + addonClass);

		JwissAddonInterface addonInstance = addonClass.getDeclaredConstructor()
			.newInstance();

		JwissLogger.printer.debug("Addon instance created for " + addonClass);

		JwissLogger.printer.debug("Caching instance of " + addonClass);

		JwissCache.addons.put(addonMetadata.name(), addonInstance);

		JwissLogger.printer
			.debug("Instance of " + addonClass + " cached with success");
	    }
	}
	JwissLogger.printer.info("Getting Class reference from JAR files Done.");
    }

    /**
     * Start cached addons
     * 
     * @return void
     */
    public void startAddons() {

	for (Entry<String, JwissAddonInterface> addon : JwissCache.addons.getMap()
		.entrySet()) {

	    JwissLogger.printer.debug("Starting " + addon.getKey() + " addon...");

	    addon.getValue().run();

	    JwissLogger.printer.debug(addon.getKey() + " addon was started.");

	}
    }
}
