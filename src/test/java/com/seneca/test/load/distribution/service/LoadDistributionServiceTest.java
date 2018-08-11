package com.seneca.test.load.distribution.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.seneca.load.distribution.service.dto.input.InputWorkLoad;
import com.seneca.load.distribution.service.dto.input.StrategyType;
import com.seneca.load.distribution.service.dto.input.TaskDistributionInput;
import com.seneca.load.distribution.service.dto.output.TaskDistributionOutput;
import com.seneca.load.distribution.service.service.LoadDistributionService;
import com.seneca.load.distribution.service.strategy.DistributionStrategy;
import com.seneca.load.distribution.service.strategy.PreferBigVendorStrategy;
import com.seneca.load.distribution.service.strategy.PreferHighestRemainingCapacityStrategy;
import com.seneca.load.distribution.service.strategy.PreferSmallVendorStrategy;

public class LoadDistributionServiceTest {

	private LoadDistributionService loadDistributionService = new LoadDistributionService();

	private List<TaskDistributionInput> createSampleTaskDistributionInput() {
		List<TaskDistributionInput> list = new ArrayList<TaskDistributionInput>();
		list.add(new TaskDistributionInput("A", 50));
		list.add(new TaskDistributionInput("B", 30));
		list.add(new TaskDistributionInput("C", 20));
		return list;
	}

	@Test
	public void testComputeDistributionWithDefaultStrategy() {
		InputWorkLoad input = new InputWorkLoad();
		input.setNumTasks(10);
		input.setDistribution(createSampleTaskDistributionInput());
		List<TaskDistributionOutput> output = loadDistributionService.distributeTasks(input, null);
		assertNotNull(output);
		assertEquals(output.size(), 3);
		for (TaskDistributionOutput o : output) {
			if (o.getVendor().equals("A")) {
				assertEquals(o.getNumTasks(), 5);
			} else if (o.getVendor().equals("B")) {
				assertEquals(o.getNumTasks(), 3);
			} else if (o.getVendor().equals("C")) {
				assertEquals(o.getNumTasks(), 2);
			}
		}
	}

	@Test
	public void testComputeDistributionWithDefaultStrategy2() {
		InputWorkLoad input = new InputWorkLoad();
		input.setNumTasks(11);
		input.setDistribution(createSampleTaskDistributionInput());
		List<TaskDistributionOutput> output = loadDistributionService.distributeTasks(input, null);
		assertNotNull(output);
		assertEquals(output.size(), 3);
		for (TaskDistributionOutput o : output) {
			if (o.getVendor().equals("A")) {
				assertEquals(o.getNumTasks(), 6);
			} else if (o.getVendor().equals("B")) {
				assertEquals(o.getNumTasks(), 3);
			} else if (o.getVendor().equals("C")) {
				assertEquals(o.getNumTasks(), 2);
			}
		}
	}

	@Test
	public void testComputeDistributionWithPreferBigStrategy() {
		InputWorkLoad input = new InputWorkLoad();
		input.setNumTasks(11);
		input.setDistribution(createSampleTaskDistributionInput());
		List<TaskDistributionOutput> output = loadDistributionService.distributeTasks(input, StrategyType.PREFER_BIG);
		assertNotNull(output);
		assertEquals(output.size(), 3);
		for (TaskDistributionOutput o : output) {
			if (o.getVendor().equals("A")) {
				assertEquals(o.getNumTasks(), 6);
			} else if (o.getVendor().equals("B")) {
				assertEquals(o.getNumTasks(), 3);
			} else if (o.getVendor().equals("C")) {
				assertEquals(o.getNumTasks(), 2);
			}
		}
	}

	@Test
	public void testComputeDistributionWithPreferSmallStrategy() {
		InputWorkLoad input = new InputWorkLoad();
		input.setNumTasks(11);
		input.setDistribution(createSampleTaskDistributionInput());
		List<TaskDistributionOutput> output = loadDistributionService.distributeTasks(input, StrategyType.PREFER_SMALL);
		assertNotNull(output);
		assertEquals(output.size(), 3);
		for (TaskDistributionOutput o : output) {
			if (o.getVendor().equals("A")) {
				assertEquals(o.getNumTasks(), 5);
			} else if (o.getVendor().equals("B")) {
				assertEquals(o.getNumTasks(), 3);
			} else if (o.getVendor().equals("C")) {
				assertEquals(o.getNumTasks(), 3);
			}
		}
	}

	private List<TaskDistributionOutput> createSampleTaskDistributionOutput() {
		List<TaskDistributionOutput> input = new ArrayList<TaskDistributionOutput>();
		TaskDistributionOutput a = new TaskDistributionOutput();
		a.setVendor("A");
		a.setAllocationPercent(50);
		a.setNumTasks(5);
		a.setRemainingCapacity(0.1);
		input.add(a);

		TaskDistributionOutput b = new TaskDistributionOutput();
		b.setVendor("B");
		b.setNumTasks(3);
		b.setRemainingCapacity(0.9);
		b.setAllocationPercent(30);
		input.add(b);

		TaskDistributionOutput c = new TaskDistributionOutput();
		c.setVendor("C");
		c.setNumTasks(2);
		c.setAllocationPercent(20);
		c.setRemainingCapacity(0.5);
		input.add(c);
		return input;
	}

	@Test
	public void testSortVendorsByPreferBigStrategy() {
		List<TaskDistributionOutput> input = createSampleTaskDistributionOutput();
		DistributionStrategy strategy = new PreferBigVendorStrategy();
		try {
			List<TaskDistributionOutput> sortedOutput = Whitebox.invokeMethod("loadDistributionService",
					"sortVendorsByStrategy", input, strategy);
			assertNotNull(sortedOutput);
			assertEquals(sortedOutput.size(), 3);
			assertEquals(sortedOutput.get(0).getVendor(), "A");
			assertEquals(sortedOutput.get(1).getVendor(), "B");
			assertEquals(sortedOutput.get(2).getVendor(), "C");
		} catch (Exception e) {
		}
	}

	@Test
	public void testSortVendorsByPreferSmallStrategy() {
		List<TaskDistributionOutput> input = createSampleTaskDistributionOutput();
		DistributionStrategy strategy = new PreferSmallVendorStrategy();
		try {
			List<TaskDistributionOutput> sortedOutput = Whitebox.invokeMethod(loadDistributionService,
					"sortVendorsByStrategy", input, strategy);
			assertNotNull(sortedOutput);
			assertEquals(sortedOutput.size(), 3);
			assertEquals(sortedOutput.get(0).getVendor(), "C");
			assertEquals(sortedOutput.get(1).getVendor(), "B");
			assertEquals(sortedOutput.get(2).getVendor(), "A");
		} catch (Exception e) {
		}
	}

	@Test
	public void testSortVendorsByPreferHighestRemainingStrategy() {
		List<TaskDistributionOutput> input = createSampleTaskDistributionOutput();
		DistributionStrategy strategy = new PreferHighestRemainingCapacityStrategy();
		try {
			List<TaskDistributionOutput> sortedOutput = Whitebox.invokeMethod(loadDistributionService,
					"sortVendorsByStrategy", input, strategy);
			assertNotNull(sortedOutput);
			assertEquals(sortedOutput.size(), 3);
			assertEquals(sortedOutput.get(0).getVendor(), "B");
			assertEquals(sortedOutput.get(1).getVendor(), "C");
			assertEquals(sortedOutput.get(2).getVendor(), "A");
		} catch (Exception e) {
		}
	}

	@Test
	public void testGetTasksAsPerShare() {
		int totalTasks = 100;
		TaskDistributionInput taskDistribution = new TaskDistributionInput("A", 40);

		TaskDistributionOutput output = loadDistributionService.computeTasksAsPerShare(totalTasks, taskDistribution);
		assertEquals(output.getVendor(), taskDistribution.getVendor());
		assertEquals(output.getNumTasks(), totalTasks * taskDistribution.getDistributionPercent() / 100);
	}

}
