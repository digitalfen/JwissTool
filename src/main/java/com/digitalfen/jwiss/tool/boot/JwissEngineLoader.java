package com.digitalfen.jwiss.tool.boot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.digitalfen.jwiss.devkit.enums.JwissActionsEnum;
import com.digitalfen.jwiss.devkit.handlers.JwissLogger;
import com.digitalfen.jwiss.devkit.interfaces.JwissLoaderInterface;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class JwissEngineLoader implements JwissLoaderInterface {

    private Map<String, String> iniCfg;

    @Setter
    private String[] args;

    @Override
    public void init() {

	/***************************************/

	JwissLogger.printer.info("Collecting system information...");

	this.iniCfg = getSystemInfo();

	JwissLogger.printer.info("System information collected.");

	JwissLogger.printer.info("Collecting profile information...");

	Map<String, String> propertiesMap = new HashMap<>();

	if (args[1].equals(JwissActionsEnum.EXECUTE.toString())) {

	    String configDirPath = new File("").getAbsolutePath();

	    String filePath = configDirPath + File.separator
		    + "config"
		    + File.separator
		    + "profiles"
		    + File.separator
		    + args[2]
		    + ".conf";

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

	}

	for (Entry<String, String> entry : propertiesMap.entrySet()) {
	    this.iniCfg.put(entry.getKey().toLowerCase(), entry.getValue());
	}

	JwissLogger.printer.info("Profile information collected.");

	/***************************************/

	JwissLogger.printer.global("Starting JwissTool Engine...");

	startJwissEngine();

	JwissLogger.printer.global("JwissTool Engine started.");

	/***************************************/

    }

    private void startJwissEngine() {

	JwissLogger.printer.info("Loading Jwiss Configurations...");

	JwissConfigLoader configLoader = new JwissConfigLoader();

	configLoader.setIniCfg(iniCfg);
	configLoader.init();

	JwissLogger.printer.info("Loading Jwiss Configurations Done.");

	/***************************************/

	JwissLogger.printer.info("Loading Jwiss Addons...");

	JwissAddonLoader addonLoader = new JwissAddonLoader();

	addonLoader.init();

	JwissLogger.printer.info("Loading Jwiss Addons Done.");

	/***************************************/

	JwissLogger.printer.info("Loading Jwiss Plugins...");

	JwissPluginLoader pluginLoader = new JwissPluginLoader();

	pluginLoader.init();

	JwissLogger.printer.info("Loading Jwiss Plugins Done.");

	/***************************************/

    }

    private Map<String, String> getSystemInfo() {

	/***************************************/

	Map<String, String> systemInfo = new HashMap<>();

	String key;
	String value;

	/***************************************/

	key = "global.initial-time";
	value = new Date().toString();
	systemInfo.put(key, value);

	/***************************************/

	key = "global.operational-system";
	value = System.getProperty("os.name");
	systemInfo.put(key, value);

	/***************************************/

	key = "global.user";
	value = System.getProperty("user.name");
	systemInfo.put(key, value);

	/***************************************/

	return systemInfo;
    };

}
