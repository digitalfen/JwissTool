package com.digitalfen.jwiss.tool.boot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.digitalfen.jwiss.devkit.handlers.JwissCache;
import com.digitalfen.jwiss.devkit.handlers.JwissLogger;
import com.digitalfen.jwiss.devkit.interfaces.JwissLoaderInterface;

import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Read, load and cache all configurations in startup.
 * 
 */
@NoArgsConstructor
public class JwissConfigLoader implements JwissLoaderInterface {

    @Setter
    private Map<String, String> iniCfg;

    /**
     * JwissLoaderInterface
     * 
     * @return void
     */
    @Override
    public void init() {
	if (iniCfg == null) {
	    iniCfg = new HashMap<>();
	}

	loadGlobalConfig();
    }

    /**
     * Load and cache global configurations
     * 
     * @return void
     */
    private void loadGlobalConfig() {

	Map<String, String> definedCfg;
	try {
	    definedCfg = readValues("global");

	    for (Entry<String, String> option : definedCfg.entrySet()) {
		JwissCache.configurations.put(
			option.getKey(),
			option.getValue());
	    }

	    JwissLogger.printer.info("Loading Global Configurations");
	    JwissLogger.printer.debug("Global Configurations File successful loaded.");
	    JwissLogger.printer.info("Loading Global Configurations Done.");
	    JwissLogger.printer.debug(JwissCache.configurations.getMap());

	} catch (

	Exception e) {
	    JwissLogger.printer.error(e);

	}

    }

    /**
     * Read external values from global.config in /config
     * 
     * @return Map<String, String>
     */
    private Map<String, String> readValues(String configName) throws Exception {
	String configDirPath = new File("").getAbsolutePath();

	String filePath = configDirPath + File.separator
		+ "config"
		+ File.separator
		+ "global"
		+ ".conf";

	Map<String, String> propertiesMap = new HashMap<>();

	try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	    String line;
	    while ((line = br.readLine()) != null) {
		line = line.trim();
		if (line.isEmpty() || line.startsWith("#") || line.startsWith(";")) {
		    continue;
		}
		String[] parts = line.split("[=:]");
		if (parts.length >= 2) {
		    String key = parts[0].trim();
		    String value = parts[1].toLowerCase().trim();
		    propertiesMap.put(key, value);
		}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();

	} catch (IOException e) {
	    e.printStackTrace();

	}

	for (Entry<String, String> entry : iniCfg.entrySet()) {
	    propertiesMap.put(entry.getKey(), entry.getValue());
	}

	return propertiesMap;

    };

}
