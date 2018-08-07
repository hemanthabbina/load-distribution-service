package com.seneca.test.load.distribution.service.dto.output;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskDistributionOutput {
	private String vendor;

	@JsonProperty(value = "num_tasks")
	private int numTasks;
}
