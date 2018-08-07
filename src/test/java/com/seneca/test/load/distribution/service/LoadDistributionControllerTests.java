package com.seneca.test.load.distribution.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.seneca.test.load.distribution.service.dto.input.InputWorkLoad;
import com.seneca.test.load.distribution.service.dto.input.TaskDistributionInput;

@RunWith(SpringRunner.class)
@SpringBootTest

public class LoadDistributionControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testLoadDistribution() throws Exception {
		mockMvc.perform(post("/distribute-load").content(getInputBody()))
		.andExpect(status().isOk())
		.andExpect(content().json(getExpectedOutput()));
	}

	private String getInputBody() {
		return "{\n" + 
				"\"tasks\" : \"100\",\n" + 
				"\"distribution\" : [\n" + 
				"	{\"vendor\": \"A\", \"distribution_percent\":\"30\"},\n" + 
				"	{\"vendor\": \"B\", \"distribution_percent\":40},\n" + 
				"	{\"vendor\": \"C\", \"distribution_percent\":30}\n" + 
				"]\n" + 
				"}";
	}

	private String getExpectedOutput() {
		return "";
	}

	private InputWorkLoad createInputDto() {
		InputWorkLoad input = new InputWorkLoad();
		input.setNumTasks(100);
		List<TaskDistributionInput> dist = new ArrayList<TaskDistributionInput>();
		dist.add(new TaskDistributionInput("A", 30));
		dist.add(new TaskDistributionInput("B", 40));
		dist.add(new TaskDistributionInput("C", 30));
		input.setDistribution(dist);
		return input;
	}

}
