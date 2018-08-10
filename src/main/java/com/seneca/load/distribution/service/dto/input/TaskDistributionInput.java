package com.seneca.load.distribution.service.dto.input;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDistributionInput {
	@NotNull
	@NotEmpty
	private String vendor;

	@Min(0)
	@Max(100)
	@JsonProperty(value = "distribution_percent")
	private int distributionPercent;
}
