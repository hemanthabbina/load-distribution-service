package com.seneca.test.load.distribution.service.service;

import java.util.Comparator;

import com.seneca.test.load.distribution.service.dto.output.TaskDistributionOutput;

public interface DistributionStrategy extends Comparator<TaskDistributionOutput> {

}
