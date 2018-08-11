package com.seneca.load.distribution.service.strategy;

import com.seneca.load.distribution.service.dto.output.TaskDistributionOutput;

public class PreferHighestRemainingCapacityStrategy implements DistributionStrategy {
	@Override
	public int compare(TaskDistributionOutput o1, TaskDistributionOutput o2) {
		if (o1.getRemainingCapacity() > o2.getRemainingCapacity()) {
			return 1;
		} else if (o1.getRemainingCapacity() < o2.getRemainingCapacity()) {
			return -1;
		}
		return 0;
	}
}
