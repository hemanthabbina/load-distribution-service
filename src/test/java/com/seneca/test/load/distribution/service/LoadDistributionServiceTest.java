package com.seneca.test.load.distribution.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.seneca.load.distribution.service.LoadDistributionServiceApplication;
import com.seneca.load.distribution.service.dto.input.InputWorkLoad;
import com.seneca.load.distribution.service.dto.input.TaskDistributionInput;
import com.seneca.load.distribution.service.dto.output.TaskDistributionOutput;
import com.seneca.load.distribution.service.service.LoadDistributionService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = LoadDistributionServiceApplication.class)
public class LoadDistributionServiceTest {

	@Autowired
	private LoadDistributionService loadDistributionService;

	@Test
	public void testComputeDistribution() {
		InputWorkLoad input = new InputWorkLoad();
		input.setNumTasks(1000);
		List<TaskDistributionInput> list = new ArrayList<TaskDistributionInput>();
		list.add(new TaskDistributionInput("A", 40));
		list.add(new TaskDistributionInput("B", 40));
		list.add(new TaskDistributionInput("C", 20));
		input.setDistribution(list);
		List<TaskDistributionOutput> output = loadDistributionService.distributeTasks();
		assertNotNull(output);
		assertEquals(output.size(), 3);
		for (TaskDistributionOutput o : output) {
			if (o.getVendor().equals("A")) {
				assertEquals(o.getNumTasks(), 400);
			} else if (o.getVendor().equals("B")) {
				assertEquals(o.getNumTasks(), 400);
			} else if (o.getVendor().equals("C")) {
				assertEquals(o.getNumTasks(), 200);
			}
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
