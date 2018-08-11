package com.seneca.load.distribution.service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.seneca.load.distribution.service.dto.input.InputWorkLoad;
import com.seneca.load.distribution.service.dto.input.StrategyType;
import com.seneca.load.distribution.service.dto.input.TaskDistributionInput;
import com.seneca.load.distribution.service.dto.output.TaskDistributionOutput;
import com.seneca.load.distribution.service.strategy.DistributionStrategy;
import com.seneca.load.distribution.service.strategy.StrategyFactory;

/**
 * Responsible for serving the requests related to load distribution
 * 
 * @author hemanth
 *
 */
@Service
public class LoadDistributionService {

	/**
	 * Entry point function, thats responsible to split the tasks across vendors as
	 * per the provided Strategy. Default strategy is to prefer the vendors with
	 * highest remaining capacity
	 * 
	 * @param input
	 * @param strategyType
	 * @return
	 */
	public List<TaskDistributionOutput> distributeTasks(InputWorkLoad input, StrategyType strategyType) {

		List<TaskDistributionOutput> initialSplit = distributeTasksAsPerPecentRoundOff(input);

		int remainingTasks = getRemainingTasksCount(input, initialSplit);
		if (remainingTasks > 0) {
			DistributionStrategy distributionStrategy = StrategyFactory.getDistributionStrategy(strategyType);
			List<TaskDistributionOutput> sortedVendors = sortVendorsByStrategy(initialSplit, distributionStrategy);
			initialSplit = allocateRemainingToSortedVendors(sortedVendors, remainingTasks);
		}
		return initialSplit;
	}

	/**
	 * Splits the remaining tasks as per the provided strategy.
	 * 
	 * @param sortedVendors
	 * @param remainingTasks
	 * @return
	 */
	private List<TaskDistributionOutput> allocateRemainingToSortedVendors(List<TaskDistributionOutput> sortedVendors,
			int remainingTasks) {
		for (int i = 0; (i < sortedVendors.size() && remainingTasks > 0); i++) {
			TaskDistributionOutput taskDist = sortedVendors.get(i);
			taskDist.setNumTasks(taskDist.getNumTasks() + 1);
			--remainingTasks;
		}
		return sortedVendors;
	}

	/**
	 * Sorts the list of vendors by strategy.
	 * 
	 * @param initialSplit
	 * @param distributionStrategy
	 * @return
	 */
	private List<TaskDistributionOutput> sortVendorsByStrategy(List<TaskDistributionOutput> initialSplit,
			DistributionStrategy distributionStrategy) {
		return initialSplit.stream().parallel().sorted(distributionStrategy.reversed()).collect(Collectors.toList());
	}

	/**
	 * Sum of allocated tasks so far.
	 * 
	 * @param initialSplit
	 * 
	 * @param initialAllocation
	 * @return
	 */
	private int getSumOfAllocatedTasksSoFar(List<TaskDistributionOutput> initialSplit) {
		return initialSplit.stream().mapToInt(i -> i.getNumTasks()).sum();
	}

	/**
	 * Gets the remaining unallocated tasks. (i.e. total - sum of allocated so far)
	 * 
	 * @param input
	 * @param initialSplit
	 * @return
	 */
	private int getRemainingTasksCount(InputWorkLoad input, List<TaskDistributionOutput> initialSplit) {
		return input.getNumTasks() - getSumOfAllocatedTasksSoFar(initialSplit);
	}

	/**
	 * Distributes the tasks as per allocated percentages, rounding off the
	 * fraction. This might leave some tasks left out.
	 * 
	 * @param input
	 * 
	 * @param input
	 * @return
	 */
	private List<TaskDistributionOutput> distributeTasksAsPerPecentRoundOff(InputWorkLoad input) {
		return input.getDistribution().stream().map(i -> computeTasksAsPerShare(input.getNumTasks(), i))
				.collect(Collectors.toList());
	}

	/**
	 * Gets the fraction part of the double.
	 * 
	 * @param output
	 * @return
	 */
	private double getFractionForPercent(double output) {
		return output - (int) output;
	}

	/**
	 * Compute the initial split for the vendor as per the given allocation percent.
	 * Also updates the meta data of remaining capacity and allocation percent.
	 * 
	 * @param totalTasks
	 * @param taskDistribution
	 * @return
	 */
	public TaskDistributionOutput computeTasksAsPerShare(int totalTasks, TaskDistributionInput taskDistribution) {
		TaskDistributionOutput output = new TaskDistributionOutput();
		output.setVendor(taskDistribution.getVendor());
		output.setAllocationPercent(taskDistribution.getDistributionPercent());
		output.setNumTasks(taskDistribution.getDistributionPercent() * totalTasks / 100);
		output.setRemainingCapacity(
				getFractionForPercent(taskDistribution.getDistributionPercent() * totalTasks * 1.0 / 100));
		return output;
	}

}
