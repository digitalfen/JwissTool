package com.digitalfen.jwiss.tool.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.digitalfen.jwiss.tool.utils.AppProperties;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Object")
@Controller
public class HealthCheck {

	private AppProperties props;

	public HealthCheck(AppProperties props) {
		this.props = props;
	}

	@ApiOperation("Health Check")
	@GetMapping
	public ResponseEntity<Object> health() {
		return ResponseEntity.status(HttpStatus.OK)
				.body("{\"nome_aplicacao\": \"" + props.getName() + "\", \"descricao_sistema\": \""
						+ props.getDescription() + "\", \"versao_sistema\": \"" + props.getVersion()
						+ "\", \"versao_java\": \"" + props.getJava() + "\"}");
	}

}
