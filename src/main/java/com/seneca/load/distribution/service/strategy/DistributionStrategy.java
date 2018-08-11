package com.seneca.load.distribution.service.strategy;

import java.util.Comparator;

import com.seneca.load.distribution.service.dto.output.TaskDistributionOutput;

public interface DistributionStrategy extends Comparator<TaskDistributionOutput> {

}
