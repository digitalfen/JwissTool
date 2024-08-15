package com.digitalfen.jwiss.tool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalfen.jwiss.devkit.dto.request.JwissInDTO;
import com.digitalfen.jwiss.devkit.dto.response.JwissOutDTO;
import com.digitalfen.jwiss.tool.service.JwissEngineService;

@RestController
@RequestMapping(value = "/api")
public class JwissEngineController {

    @Autowired
    private JwissEngineService service;

    @PostMapping(value = "/execute")
    public JwissOutDTO execute(@RequestBody JwissInDTO inDTO) {

	JwissOutDTO out = service.execute(inDTO);

	return out;
    }

}
