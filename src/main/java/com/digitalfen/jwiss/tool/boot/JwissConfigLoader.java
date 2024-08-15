package com.digitalfen.jwiss.tool.boot;

import java.util.Map;
import java.util.Map.Entry;

import com.digitalfen.jwiss.devkit.enums.JwissConfigLabelEnum;
import com.digitalfen.jwiss.devkit.handlers.JwissCache;
import com.digitalfen.jwiss.devkit.handlers.JwissConfiguration;
import com.digitalfen.jwiss.devkit.handlers.JwissLogger;
import com.digitalfen.jwiss.devkit.handlers.JwissUtils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwissConfigLoader {

    /**
     * Read, load and cache all configurations. Method by method.
     */
    public void init() {
	loadGlobalConfig();
    }

    /**
     * Read, load and cache global configurations
     */
    private void loadGlobalConfig() {

	Map<String, String> definedCfg;
	try {
	    definedCfg = JwissConfiguration.io.readValues(JwissConfigLabelEnum.GLOBAL);

	    for (Entry<String, String> option : definedCfg.entrySet()) {
		JwissCache.configurations.put(null,
			option.getKey(),
			option.getValue());
	    }

	    JwissLogger.printer.info("Loading Global Configurations");
	    JwissLogger.printer.debug("Global Configurations File successful loaded.");
	    JwissLogger.printer.debug(
		    JwissUtils.text.writeTable(JwissCache.configurations.getAll()));
	    JwissLogger.printer.info("Loading Global Configurations Done.");
	    
	    

	} catch (Exception e) {
	    JwissLogger.printer.error(e);

	}

    }

}
