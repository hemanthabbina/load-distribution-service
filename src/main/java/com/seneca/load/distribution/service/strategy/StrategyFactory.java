package com.seneca.load.distribution.service.strategy;

import com.seneca.load.distribution.service.dto.input.StrategyType;

/**
 * Factory class responsible to create the strategy classes based on the provided type.
 * 
 * @author hemanth
 *
 */
public class StrategyFactory {

	/**
	 * Default strategy is PREFER_HIGHEST_REMAINING
	 * 
	 * @param strategyType
	 * @return
	 */
	public static DistributionStrategy getDistributionStrategy(StrategyType strategyType) {
		DistributionStrategy strategy = null;
		if (StrategyType.PREFER_BIG.equals(strategyType)) {
			strategy = new PreferBigVendorStrategy();
		} else if (StrategyType.PREFER_SMALL.equals(strategyType)) {
			strategy = new PreferSmallVendorStrategy();
		} else {
			strategy = new PreferHighestRemainingCapacityStrategy();
		}
		return strategy;
	}
}
