package com.example.resources.example;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.example.Application;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class ExampleControllerIT {
  
  private static final String[] NAMES = { //@formatter:off
      "TEST Name 001",
      "test NAME 2"
  }; //@formatter:on
    
  @Autowired
  private ExampleRepository exampleRepository;

  @Value("${local.server.port}")
  private int port;

	private URL base;
	private RestTemplate template;

	@Before
	public void setUp() throws Exception {
	  exampleRepository.deleteAll();
		this.base = new URL("http://localhost:" + port + "/examples");
		template = new TestRestTemplate();
	}
	
	@Test
	public void getExamples_shouldInitiallyBeEmpty() throws Exception {
    ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
    assertThat(response.getBody(), equalTo("[]"));
	}
	
	@Test
	public void postExample_shouldReturnInputWithIdField() throws Exception {
	  
	  // Create an argument to pass into the mock HTTP Post request
	  final Example argument = new Example();
	  argument.setName(NAMES[0]);
	  
	  // Make the mock HTTP Post request and get the response
	  final ResponseEntity<String> response
	      = template.postForEntity(base.toString(), argument, String.class);
	  final String jsonBody = response.getBody();
	  final Example body = parseJson(jsonBody);
	  
	  // get the JSON string expected in the response body
	  final long actualId = body.getId();
	  argument.setId(actualId);
	  final String expectedBody = toJson(argument);
	  
	  assertThat(response.getBody(), equalTo(expectedBody));
	}
  
  @Test
  public void getExamples_shouldReturnPostedExamples() throws Exception {
    
    final List<Example> expectedExamples = new ArrayList<>();
    
    for (int i = 0; i < NAMES.length; ++i) {
      
      // Create an argument to pass into the mock HTTP Post request
      final Example argument = new Example();
      argument.setName(NAMES[i]);
      
      // Make the mock HTTP Post request and get the response
      final ResponseEntity<String> response
          = template.postForEntity(base.toString(), argument, String.class);
      final String jsonBody = response.getBody();
      final Example body = parseJson(jsonBody);
      
      // add the ID to the argument and add it to the list of expected examples
      final long actualId = body.getId();
      argument.setId(actualId);
      expectedExamples.add(argument);      
    }
    
    // Make the most HTTP Get request and get the response
    ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
    
    // Assert that the response is equal to the JSON string of the expected list of examples
    assertThat(response.getBody(), equalTo(toJson(expectedExamples)));
  }
  
  @Test
  public void putExample_shouldReturnUpdatedExample() throws JsonParseException, JsonMappingException, IOException {
    
    final List<Long> expectedIds = new ArrayList<>();    
    final List<Example> expectedExamples = new ArrayList<>();
    
    for (int i = 0; i < NAMES.length; ++i) {
      
      // Create an argument to pass into the mock HTTP Post request
      final Example argument = new Example();
      argument.setName(NAMES[i]);
      
      // Make the mock HTTP Post request and get the response
      final ResponseEntity<String> response
          = template.postForEntity(base.toString(), argument, String.class);
      final String jsonBody = response.getBody();
      final Example body = parseJson(jsonBody);
      
      // keep track of the resource and its id
      final long actualId = body.getId();
      expectedIds.add(actualId);
      expectedExamples.add(argument);      
    }
    
    for (int i = 0; i < expectedExamples.size() && i < expectedIds.size(); ++i) {
      
      final Example expectedExample = expectedExamples.get(i);
      
      // Modify the example
      expectedExample.setName(NAMES[i] + "_MODIFIED");
      
      // Make the mock HTTP Put request
      template.put(base.toString() + "/" + expectedIds.get(i), expectedExample);
      
      // Add the ID to the expected example
      expectedExample.setId(expectedIds.get(i));
    }
    
    // Make the most HTTP Get request and get the response
    ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
    
    // Assert that the response is equal to the JSON string of the expected list of examples
    assertThat(response.getBody(), equalTo(toJson(expectedExamples)));
  }
	
	private String toJson(final Example example) throws JsonProcessingException {
	  return new ObjectMapper().writeValueAsString(example);
	}
	
	private String toJson(final Collection<Example> examples) throws JsonProcessingException {
	  return new ObjectMapper().writeValueAsString(examples);
	}
	
	private Example parseJson(final String jsonExample) throws JsonParseException, JsonMappingException, IOException {
	  return new ObjectMapper().readValue(jsonExample, Example.class);
	}
}
