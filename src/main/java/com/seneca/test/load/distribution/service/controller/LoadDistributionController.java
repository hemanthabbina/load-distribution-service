package com.seneca.test.load.distribution.service.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.seneca.test.load.distribution.service.dto.input.InputWorkLoad;
import com.seneca.test.load.distribution.service.dto.input.validator.InputWorkLoadValidator;
import com.seneca.test.load.distribution.service.dto.output.TaskDistributionOutput;
import com.seneca.test.load.distribution.service.service.LoadDistributionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "load-distribtion-service", description = "Service responsible for distributing workload among workers")
@RestController
public class LoadDistributionController {
	@Autowired
	InputWorkLoadValidator inputWorkLoadValidator;

	@Autowired
	LoadDistributionService loadDistributionService;

	@ApiOperation(value = "Distribute the workload")
	@PostMapping("/distribute-load")
	public ResponseEntity<List<TaskDistributionOutput>> computeDistribution(@Valid @RequestBody InputWorkLoad input,
			BindingResult bindingResult) throws Exception {

		inputWorkLoadValidator.validate(input, bindingResult);
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(loadDistributionService.computeDistribution(input));
	}

}
