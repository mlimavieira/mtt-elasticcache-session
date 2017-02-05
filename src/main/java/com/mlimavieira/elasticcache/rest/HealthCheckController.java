package com.mlimavieira.elasticcache.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@RequestMapping(value = "/hc/status")
	public String getStatus() {

		return "Ok!!!";
	}
}
