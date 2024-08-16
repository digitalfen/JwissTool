package com.digitalfen.jwiss.tool.service;

import java.lang.reflect.Method;

import org.springframework.stereotype.Service;

import com.digitalfen.jwiss.devkit.annotations.JwissCommandMetadata;
import com.digitalfen.jwiss.devkit.dto.request.JwissInDTO;
import com.digitalfen.jwiss.devkit.dto.response.JwissOutDTO;
import com.digitalfen.jwiss.devkit.handlers.JwissCache;
import com.digitalfen.jwiss.devkit.handlers.JwissLogger;
import com.digitalfen.jwiss.devkit.interfaces.JwissPluginInterface;
import com.digitalfen.jwiss.devkit.model.Argument;
import com.digitalfen.jwiss.devkit.model.Command;

/**
 * Engine Service for commands execution
 */
@Service
public class JwissEngineService {

    /**
     * Engine Service for commands execution
     * 
     * @param inDTO JwissInDTO;
     * @return JwissOutDTO;
     */
    public JwissOutDTO execute(JwissInDTO inDTO) {

	JwissOutDTO out = new JwissOutDTO();

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
				    JwissCache.configurations.put(plugin,
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

	return out;
    }

//    private JwissPluginInterface executeCommand(JwissCommand command) {
//	// Recupera a instância do plugin a partir do JwissCache
//	JwissPluginInterface pluginInstance = JwissCache.plugins
//		.get(command.getParentName());
//
//	if (pluginInstance == null) {
//	    System.out.println("Plugin não encontrado: " + command.getParentName());
//	    return pluginInstance;
//	}
//
//	Class<?> pluginClass = pluginInstance.getClass();
//	try {
//	    // Itera sobre os métodos da classe para encontrar o método anotado com
//	    // JwissCommandMetadata
//	    for (Method method : pluginClass.getDeclaredMethods()) {
//		if (method.isAnnotationPresent(JwissCommandMetadata.class)) {
//		    JwissCommandMetadata commandMetadata = method
//			    .getAnnotation(JwissCommandMetadata.class);
//		    if (commandMetadata.usage().equals(command.getUsage())) {
//			// Invoca o método que corresponde ao comando fornecido
//
//			method.invoke(pluginInstance);
//		    }
//		}
//	    }
//
//	    System.out.println("Comando não encontrado: " + command.getUsage());
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
//
//	return pluginInstance;
//    }

}
