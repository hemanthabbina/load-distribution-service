package com.seneca.load.distribution.service.dto.input;

public enum StrategyType {
	PREFER_BIG("prefer-big-vendor"), PREFER_SMALL("prefer-small-vendor"), PREFER_HIGHEST_REMAINING(
			"prefer-highest-remaining-capacity-vendor");

	private String prettyName;

	StrategyType(String name) {
		this.prettyName = name;
	}

	public String getPrettyName() {
		return prettyName;
	}

	public void setPrettyName(String prettyName) {
		this.prettyName = prettyName;
	}

}
