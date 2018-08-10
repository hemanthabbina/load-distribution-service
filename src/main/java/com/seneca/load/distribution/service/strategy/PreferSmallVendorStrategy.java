package com.seneca.load.distribution.service.strategy;

import com.seneca.load.distribution.service.dto.output.TaskDistributionOutput;
import com.seneca.load.distribution.service.service.DistributionStrategy;

public class PreferSmallVendorStrategy implements DistributionStrategy {
	@Override
	public int compare(TaskDistributionOutput o1, TaskDistributionOutput o2) {
		if (o1.getAllocationPercent() < o2.getAllocationPercent()) {
			return 1;
		} else if (o1.getAllocationPercent() > o2.getAllocationPercent()) {
			return -1;
		}
		return 0;
	}
}
