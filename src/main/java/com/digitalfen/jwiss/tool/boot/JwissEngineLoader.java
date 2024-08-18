package com.digitalfen.jwiss.tool.boot;

import com.digitalfen.jwiss.devkit.handlers.JwissLogger;
import com.digitalfen.jwiss.devkit.interfaces.JwissLoaderInterface;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class JwissEngineLoader implements JwissLoaderInterface {

    @Setter
    private String[] args;

    @Override
    public void init() {

	JwissLogger.printer.global("Starting JwissTool Engine...");

	startJwissEngine();

	JwissLogger.printer.global("JwissTool Engine started.");

	/***************************************/
	
//	JwissLogger.printer.global("Starting JwissTool Engine...");
//
//	startJwissEngine();
//
//	JwissLogger.printer.global("JwissTool Engine started.");

    }

    private void startJwissEngine() {

	JwissLogger.printer.info("Loading Jwiss Configurations...");

	JwissConfigLoader configLoader = new JwissConfigLoader();

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
    }

}
