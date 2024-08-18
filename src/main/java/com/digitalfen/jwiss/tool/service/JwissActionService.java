package com.digitalfen.jwiss.tool.service;

import java.util.Map.Entry;

import com.digitalfen.jwiss.devkit.enums.JwissActionsEnum;
import com.digitalfen.jwiss.devkit.handlers.JwissCache;
import com.digitalfen.jwiss.devkit.handlers.JwissLogger;
import com.digitalfen.jwiss.devkit.handlers.JwissUtils;
import com.digitalfen.jwiss.devkit.interfaces.JwissServiceInterface;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class JwissActionService implements JwissServiceInterface {

    @Setter
    private String[] input;

    @Override
    public void run() {
	parseAppArgs(input);

    }

    public void parseAppArgs(String[] args) {

	JwissActionsEnum currentAction = null;

	boolean exec = false;
	boolean save = false;

	for (int i = 0; i < args.length; i++) {

	    if (i == 0) {
		continue;
	    }

	    if (args[i].toUpperCase()
		    .equals(JwissActionsEnum.CONFIGURATIONS.toString())) {
		currentAction = JwissActionsEnum.CONFIGURATIONS;
		save = true;
		continue;

	    } else if (args[i].toUpperCase().equals(JwissActionsEnum.SAVE.toString())) {
		currentAction = JwissActionsEnum.SAVE;
		save = true;
		continue;

	    } else if (args[i].toUpperCase()
		    .equals(JwissActionsEnum.COMMANDS.toString())) {
		currentAction = JwissActionsEnum.COMMANDS;

		continue;

	    } else if (args[i].toUpperCase()
		    .equals(JwissActionsEnum.EXECUTE.toString())) {
		currentAction = JwissActionsEnum.EXECUTE;
		exec = true;
		continue;

	    } else if (args[i].toUpperCase().equals(JwissActionsEnum.ADDONS.toString())) {
		currentAction = JwissActionsEnum.ADDONS;
		save = true;

		continue;

	    } else if (args[i].toUpperCase()
		    .equals(JwissActionsEnum.PLUGINS.toString())) {
		currentAction = JwissActionsEnum.PLUGINS;
		save = true;

		continue;

	    } else {
		if (currentAction == JwissActionsEnum.CONFIGURATIONS) {
		    String[] splitedParam = args[i].split("=");
		    String key = splitedParam[0]; // TODO add validation
		    String value = splitedParam[0];

		    JwissCache.configurations.put(key, value);
		} else if (currentAction == JwissActionsEnum.SAVE && save && !exec) {
		    String toSave = new String();

		    toSave = toSave
			    .concat("global.active-addons="
				    + JwissCache.configurations.get("active-addons"))
			    + "\n";
		    toSave = toSave
			    .concat("global.active-plugins="
				    + JwissCache.configurations.get("active-plugins"))
			    + "\n";
		    toSave = toSave
			    .concat("global.starting-commands="
				    + JwissCache.configurations.get("starting-commands"))
			    + "\n";
		    for (Entry<String, String> entry : JwissCache.configurations.getMap()
			    .entrySet()) {
			toSave = toSave
				.concat(entry.getKey() + "=" + entry.getValue()) + "\n";

		    }

		    JwissUtils.io.writeLineToFile("config/profiles/" + args[i] + ".conf",
			    toSave); // TODO refazer o arquivo dfe profile (montar um map)

		    System.exit(0);

		} else if (currentAction.equals(JwissActionsEnum.ADDONS)) {
		    String appendValue = JwissCache.configurations
			    .get("active-addons") == null ? ""
				    : JwissCache.configurations.get("active-addons")
					    .toLowerCase()
					    + ",";

		    JwissCache.configurations.put("active-addons",
			    appendValue + args[i].trim());

		} else if (currentAction.equals(JwissActionsEnum.COMMANDS)) {
		    String appendValue = JwissCache.configurations
			    .get("starting-commands") == null ? ""
				    : JwissCache.configurations.get("starting-commands")
					    .toLowerCase()
					    + " ";

		    JwissCache.configurations.put("starting-commands",
			    appendValue + args[i].trim());

		} else if (currentAction.equals(JwissActionsEnum.PLUGINS)) {
		    String appendValue = JwissCache.configurations
			    .get("active-plugins") == null ? ""
				    : JwissCache.configurations.get("active-plugins")
					    .toLowerCase()
					    + ",";

		    JwissCache.configurations.put("active-plugins",
			    appendValue + args[i].trim());

		} else if (currentAction.equals(JwissActionsEnum.EXECUTE) && !save
			&& exec) {
		    break; // TODO bolar exeecucao a partir do profile

		} else {
		    JwissLogger.printer.error("Unknown action command!");
		    System.exit(1);

		}
	    }

	}

    }

}
