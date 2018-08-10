package com.seneca.test.load.distribution.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.seneca.load.distribution.service.LoadDistributionServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = LoadDistributionServiceApplication.class)

public class LoadDistributionControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	private String url = "/load-distribution-service/distribute";

	/**
	 * Test with valid input and valid output.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoadDistributionWithValidInputs() throws Exception {
		mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(getInputBody())).andExpect(status().isOk())
				.andExpect(content().json(getExpectedOutput()));
	}

	/**
	 * With invalid content type - 415
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoadDistributionWithInvalidContentType() throws Exception {
		mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_XML).content(getInputBody()))
				.andExpect(status().isUnsupportedMediaType());
	}

	/**
	 * With invalid URL - 404
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoadDistributionWithWrongUrl() throws Exception {
		mockMvc.perform(post("/dummy-url").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(getInputBody())).andExpect(status().isNotFound());
	}

	/**
	 * With invalid input json - 400
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoadDistributionWithInvalidInputJson() throws Exception {
		mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(getInvalidInputBody()))
				.andExpect(status().isBadRequest());
	}

	/**
	 * With invalid total tasks (negative number) - 400
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoadDistributionWithInputValidationOnTotalTasks() throws Exception {
		mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(getInputBodyWithInvalidTasks()))
				.andExpect(status().isBadRequest());
	}

	/**
	 * With invalid individual share percent (>100%) - 400
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoadDistributionWithInputValidationOnIndividualSharePercent() throws Exception {
		mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(getInputBodyWithInvalidIndividualSharePercent()))
				.andExpect(status().isBadRequest());
	}

	/**
	 * With invalid total share (sum of share percents is > 100)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoadDistributionWithInputValidationOnTotalSharePercent() throws Exception {
		mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(getInputBodyWithTotalSharePercent()))
				.andExpect(status().isBadRequest());
	}

	private String getInputBodyWithInvalidTasks() {
		return "{\n" + "\"tasks\" : \"-10\",\n" + "\"distribution\" : [\n"
				+ "	{\"vendor\": \"A\", \"distribution_percent\":\"30\"},\n"
				+ "	{\"vendor\": \"B\", \"distribution_percent\":40},\n"
				+ "	{\"vendor\": \"C\", \"distribution_percent\":30}\n" + "]\n" + "}";
	}

	private String getInputBodyWithInvalidIndividualSharePercent() {
		return "{\n" + "\"tasks\" : \"100\",\n" + "\"distribution\" : [\n"
				+ "	{\"vendor\": \"A\", \"distribution_percent\":\"130\"},\n"
				+ "	{\"vendor\": \"B\", \"distribution_percent\":40},\n"
				+ "	{\"vendor\": \"C\", \"distribution_percent\":30}\n" + "]\n" + "}";
	}

	private String getInputBodyWithTotalSharePercent() {
		return "{\n" + "\"tasks\" : \"100\",\n" + "\"distribution\" : [\n"
				+ "	{\"vendor\": \"A\", \"distribution_percent\":\"90\"},\n"
				+ "	{\"vendor\": \"B\", \"distribution_percent\":40},\n"
				+ "	{\"vendor\": \"C\", \"distribution_percent\":30}\n" + "]\n" + "}";
	}

	private String getInputBody() {
		return "{\n" + "\"tasks\" : \"100\",\n" + "\"distribution\" : [\n"
				+ "	{\"vendor\": \"A\", \"distribution_percent\":\"30\"},\n"
				+ "	{\"vendor\": \"B\", \"distribution_percent\":40},\n"
				+ "	{\"vendor\": \"C\", \"distribution_percent\":30}\n" + "]\n" + "}";
	}

	/**
	 * Invalid JSON text
	 */
	private String getInvalidInputBody() {
		return "\"tasks\" : \"100\",\n" + "\"distribution\" : [\n"
				+ "	{\"vendor\": \"A\", \"distribution_percent\":\"30\"},\n"
				+ "	{\"vendor\": \"B\", \"distribution_percent\":40},\n"
				+ "	{\"vendor\": \"C\", \"distribution_percent\":30}\n" + "]\n" + "}";
	}

	private String getExpectedOutput() {
		return "[{\"vendor\":\"A\",\"num_tasks\":30},{\"vendor\":\"B\",\"num_tasks\":40},{\"vendor\":\"C\",\"num_tasks\":30}]";
	}

}
