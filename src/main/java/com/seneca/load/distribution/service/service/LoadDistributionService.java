package com.seneca.load.distribution.service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.seneca.load.distribution.service.dto.input.InputWorkLoad;
import com.seneca.load.distribution.service.dto.input.TaskDistributionInput;
import com.seneca.load.distribution.service.dto.output.TaskDistributionOutput;

import lombok.Getter;
import lombok.Setter;

@Service
@Setter
@Getter
public class LoadDistributionService {
	private InputWorkLoad input;
	private List<TaskDistributionOutput> output;
	private DistributionStrategy strategy;

	public List<TaskDistributionOutput> distributeTasks() {
		if (input == null || input.getDistribution() == null || input.getDistribution().isEmpty()) {
			return new ArrayList<TaskDistributionOutput>();
		}

		distributeTasksAsPerPecentRoundOff();

		int remainingTasks = getRemainingTasksCount();

		if (remainingTasks > 0) {
			sortVendorsByStrategy();
			System.out.println("Sorted vendors");
			for (int i = 0; (i < output.size()); i++) {
				System.out.println(output.get(i).getVendor() + "   " + output.get(i).getNumTasks() + "   "
						+ output.get(i).getVendor());
			}
			for (int i = 0; (i < output.size() && remainingTasks > 0); i++) {
				TaskDistributionOutput taskDist = output.get(i);
				taskDist.setNumTasks(taskDist.getNumTasks() + 1);
				--remainingTasks;
			}
		}
		return output;
	}

	private void sortVendorsByStrategy() {
		output = output.stream().parallel().sorted(this.strategy.reversed()).collect(Collectors.toList());
	}

	/**
	 * Sum of allocated tasks so far.
	 * 
	 * @param initialAllocation
	 * @return
	 */
	private int getSumOfAllocatedTasksSoFar() {
		return this.output.stream().mapToInt(i -> i.getNumTasks()).sum();
	}

	private int getRemainingTasksCount() {
		return input.getNumTasks() - getSumOfAllocatedTasksSoFar();
	}

	/**
	 * Distributes the tasks as per allocated percentages, rounding off the
	 * fraction. This might leave some tasks left out.
	 * 
	 * @param input
	 * @return
	 */
	private void distributeTasksAsPerPecentRoundOff() {
		this.output = input.getDistribution().stream().map(i -> computeTasksAsPerShare(input.getNumTasks(), i))
				.collect(Collectors.toList());
	}

	private double getFractionForPercent(double output) {
		return output - (int) output;
	}

	public TaskDistributionOutput computeTasksAsPerShare(int totalTasks, TaskDistributionInput taskDistribution) {
		TaskDistributionOutput output = new TaskDistributionOutput();
		output.setVendor(taskDistribution.getVendor());
		output.setAllocationPercent(taskDistribution.getDistributionPercent());
		output.setNumTasks((int) taskDistribution.getDistributionPercent() * totalTasks / 100);
		output.setRemainingCapacity(getFractionForPercent(taskDistribution.getDistributionPercent() * 1.0 / 100));
		return output;
	}

}
