package com.seneca.test.load.distribution.service.dto.input;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InputWorkLoad {
	@JsonProperty(value = "tasks")
	@Positive
	private int numTasks;

	@Valid
	private List<TaskDistributionInput> distribution;

}
