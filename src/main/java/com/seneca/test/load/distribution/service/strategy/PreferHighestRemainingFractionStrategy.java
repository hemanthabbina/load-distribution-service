package com.seneca.test.load.distribution.service.strategy;

import com.seneca.test.load.distribution.service.dto.output.TaskDistributionOutput;
import com.seneca.test.load.distribution.service.service.DistributionStrategy;

public class PreferHighestRemainingFractionStrategy implements DistributionStrategy {
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
