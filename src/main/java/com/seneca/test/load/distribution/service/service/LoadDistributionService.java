package com.seneca.test.load.distribution.service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.seneca.test.load.distribution.service.dto.input.InputWorkLoad;
import com.seneca.test.load.distribution.service.dto.input.TaskDistributionInput;
import com.seneca.test.load.distribution.service.dto.output.TaskDistributionOutput;

@Service
public class LoadDistributionService {

	public List<TaskDistributionOutput> computeDistribution(InputWorkLoad input) {
		List<TaskDistributionInput> taskDistribution = input.getDistribution();
		return input.getDistribution().stream().parallel().map(i -> getTasksAsPerShare(input.getNumTasks(), i))
				.collect(Collectors.toList());
	}

	private TaskDistributionOutput getTasksAsPerShare(int totalTasks, TaskDistributionInput taskDistribution) {
		TaskDistributionOutput output = new TaskDistributionOutput();
		output.setVendor(taskDistribution.getVendor());
		output.setNumTasks((int) taskDistribution.getDistributionPercent() * totalTasks / 100);
		return output;
	}

}
