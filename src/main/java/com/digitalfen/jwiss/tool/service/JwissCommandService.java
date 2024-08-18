package com.digitalfen.jwiss.tool.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.digitalfen.jwiss.devkit.annotations.JwissCommandMetadata;
import com.digitalfen.jwiss.devkit.annotations.JwissServiceAnnotation;
import com.digitalfen.jwiss.devkit.dto.request.JwissInDTO;
import com.digitalfen.jwiss.devkit.dto.response.JwissOutDTO;
import com.digitalfen.jwiss.devkit.handlers.JwissCache;
import com.digitalfen.jwiss.devkit.handlers.JwissLogger;
import com.digitalfen.jwiss.devkit.interfaces.JwissPluginInterface;
import com.digitalfen.jwiss.devkit.interfaces.JwissServiceInterface;
import com.digitalfen.jwiss.devkit.model.Argument;
import com.digitalfen.jwiss.devkit.model.Command;

import lombok.NoArgsConstructor;

/**
 * Engine Service for commands execution
 */
@JwissServiceAnnotation
@NoArgsConstructor
public class JwissCommandService implements JwissServiceInterface {

    private JwissInDTO inDTO = new JwissInDTO();
    private JwissOutDTO outDTO = new JwissOutDTO();

    @Override
    public void run() {
	execute(this.inDTO);
    }

    /**
     * Engine Service for commands execution
     * 
     * @param inDTO JwissInDTO;
     * @return JwissOutDTO;
     */
    public void execute(JwissInDTO inDTO) {

	try {

	    for (Command command : inDTO.getCommands()) {

		JwissPluginInterface plugin = JwissCache.plugins
			.get(command.getParentName());

		if (plugin != null) {

		    Class<?> pluginClass = plugin.getClass();

		    // Itera sobre os métodos da classe para encontrar o método anotado
		    // com JwissCommandMetadata

		    Method toExecute = null;

		    for (Method method : pluginClass.getDeclaredMethods()) {

			if (method.isAnnotationPresent(JwissCommandMetadata.class)) {
			    JwissCommandMetadata commandMetadata = method
				    .getAnnotation(JwissCommandMetadata.class);
			    if (commandMetadata.usage().equals(command.getUsage())) {

				for (Argument argument : command.getArguments()) {
				    JwissCache.configurations.put(
					    plugin,
					    argument.getKey(),
					    argument.getValue());

				}

				toExecute = method;
			    }
			}

		    }

		    if (toExecute != null) {
			toExecute.invoke(plugin);

		    } else {
			throw new Exception(
				"Comando não encontrado: " + command.getUsage());
		    }

		} else {
		    throw new Exception(
			    "Plugin não encontrado: " + command.getParentName());

		}

	    }

	} catch (Exception e) {
	    JwissLogger.printer.error(e.getMessage());
	}

    }

    /**
     * Parse an Input String into List of Commands
     * 
     * @param input String
     * @return List<Command>
     */
    public void setInput(String input) {
	List<Command> out = new ArrayList<>();

	List<String> commands = Arrays
		.asList(input != null ? input.split(";") : "".split("")); // TODO de
									  // acordo com so

	for (String cmd : commands) {

	    Command command = new Command();
	    List<Argument> arguments = new ArrayList<>();

	    String[] splittedCommand = cmd.split(" ");

	    for (int i = 0; i < splittedCommand.length; i++) {

		if (i == 0) {
		    command.setParentName(splittedCommand[i]);
		} else if (i == 1) {
		    command.setUsage(splittedCommand[i]);
		} else if (i > 1) {

		    if (splittedCommand[i].contains("-")) {
			Argument argument = new Argument();
			argument.setKey(splittedCommand[i].replace("-", ""));

			if (i != splittedCommand.length - 1
				&& splittedCommand[i + 1] != null
				&& !splittedCommand[i + 1].contains("-")) {
			    argument.setValue(splittedCommand[i + 1]);
			}

			arguments.add(argument);

		    }
		}

	    }

	    command.setArguments(arguments);
	    out.add(command);

	}

	this.inDTO.setCommands(out);
    }

}
