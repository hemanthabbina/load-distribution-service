package com.seneca.load.distribution.service.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seneca.load.distribution.service.dto.input.InputWorkLoad;
import com.seneca.load.distribution.service.dto.input.validator.InputWorkLoadValidator;
import com.seneca.load.distribution.service.dto.output.TaskDistributionOutput;
import com.seneca.load.distribution.service.service.LoadDistributionService;
import com.seneca.load.distribution.service.strategy.PreferBigVendorStrategy;
import com.seneca.load.distribution.service.strategy.PreferHighestRemainingFractionStrategy;
import com.seneca.load.distribution.service.strategy.PreferSmallVendorStrategy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "load-distribution-service", description = "Service responsible for distributing workload among workers")
@RestController
@RequestMapping("/load-distribution-service")
public class LoadDistributionController {
	@Autowired
	InputWorkLoadValidator inputWorkLoadValidator;

	@Autowired
	LoadDistributionService loadDistributionService;

	@ApiOperation(value = "Distribute the workload")
	@PostMapping("/distribute/prefer-big-vendor")
	public ResponseEntity<List<TaskDistributionOutput>> computeDistributionWithPreferBigVendor(
			@Valid @RequestBody InputWorkLoad input, BindingResult bindingResult) throws Exception {

		inputWorkLoadValidator.validate(input, bindingResult);
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(null);
		}
		loadDistributionService.setInput(input);
		loadDistributionService.setStrategy(new PreferBigVendorStrategy());
		return ResponseEntity.status(HttpStatus.OK).body(loadDistributionService.distributeTasks());
	}

	@ApiOperation(value = "Distribute the workload")
	@PostMapping("/distribute/prefer-small-vendor")
	public ResponseEntity<List<TaskDistributionOutput>> computeDistributionWithPreferSmallVendor(
			@Valid @RequestBody InputWorkLoad input, BindingResult bindingResult) throws Exception {

		inputWorkLoadValidator.validate(input, bindingResult);
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(null);
		}
		loadDistributionService.setInput(input);
		loadDistributionService.setStrategy(new PreferSmallVendorStrategy());
		return ResponseEntity.status(HttpStatus.OK).body(loadDistributionService.distributeTasks());
	}

	@ApiOperation(value = "Distribute the workload")
	@PostMapping("/distribute/prefer-highest-remaining-fraction-vendor")
	public ResponseEntity<List<TaskDistributionOutput>> computeDistributionWithPerferHighestRemaining(
			@Valid @RequestBody InputWorkLoad input, BindingResult bindingResult) throws Exception {

		inputWorkLoadValidator.validate(input, bindingResult);
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(null);
		}
		loadDistributionService.setInput(input);
		loadDistributionService.setStrategy(new PreferHighestRemainingFractionStrategy());
		return ResponseEntity.status(HttpStatus.OK).body(loadDistributionService.distributeTasks());
	}

}
