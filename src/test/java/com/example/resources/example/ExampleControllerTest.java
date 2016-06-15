package com.example.resources.example;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Equals;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
@DirtiesContext
public class ExampleControllerTest {

  private static final int NUM_EXAMPLES = 5;

  private MockMvc mvc;

  @InjectMocks
  @Autowired
  ExampleController exampleController;

  @Mock
  ExampleRepository mockExampleRepository;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.standaloneSetup(exampleController).build();
  }

  @Test
  public void getExamples_shouldReturnCollectionReturnedByMockRepository() throws Exception {

    for (int i = 0; i < NUM_EXAMPLES; ++i) {
      final Collection<Example> testCollection = getTestCollection(i);
      when(mockExampleRepository.findAll()).thenReturn(testCollection);
      mvc.perform(MockMvcRequestBuilders.get("/examples").accept(MediaType.APPLICATION_JSON)) //@formatter:off
          .andExpect(status().isOk())
          .andExpect(content().string(equalTo(toJson(testCollection)))); //@formatter:on

      // Verify that mock repository findAll was called once
      verify(mockExampleRepository, times(1)).findAll();

      // Clear all previous interactions with the mock repository
      // in preparation for the next test interval
      reset(mockExampleRepository);
    }
  }

  @Test
  public void getExample_shouldReturnExampleReturnedByMockRepository() throws Exception {

    final Example expected = new Example();
    final long id = 1;

    when(mockExampleRepository.findOne(id)).thenReturn(expected);

    mvc.perform(MockMvcRequestBuilders.get("/examples/" + id) //@formatter:off
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo(toJson(expected)))); //@formatter:on

    // Verify that mock repository findOne was called for the given id
    verify(mockExampleRepository, times(1)).findOne(id);
  }

  @Test
  public void getExample_ifNoExampleWithGivenId_shouldReturn400Error() throws Exception {

    final long id = 1;

    when(mockExampleRepository.findOne(id)).thenReturn(null);

    mvc.perform(MockMvcRequestBuilders.get("/examples/" + id) //@formatter:off
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400));
    
    // Verify that mock repository findOne was called once for the given id
    verify(mockExampleRepository, times(1)).findOne(id);
  }
  
  @Test
  public void postExample_shouldCallRepositorySaveWithExample()
      throws JsonProcessingException, Exception {
    
    final Example input = new Example();
    final Example expected = new Example();
    expected.setId(1);
    
    // When the mock repository save method is called for an example matching the input
    // then return the expected output
    when(mockExampleRepository.save((Example) argThat(new Equals(input))))
        .thenReturn(expected);
    
    // Perform an HTTP POST request with the input as JSON
    // and verify that status 200 was received
    // and verify that the expected output was received
    mvc.perform(MockMvcRequestBuilders.post("/examples") //@formatter:off
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(input))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo(toJson(expected)))); //@formatter:on

    // Verify that the mock repository save method was called with an example
    // matching the input
    verify(mockExampleRepository, times(1)).save((Example) argThat(new Equals(input)));
  }

  @Test
  public void postExample_ifContainsIdField_shouldReturn400Error()
      throws JsonProcessingException, Exception {

    final Example input = new Example();
    input.setId(1);

    // Perform an HTTP POST request with the input as JSON
    // and verify that status 400 was received
    mvc.perform(MockMvcRequestBuilders.post("/examples") //@formatter:off
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(input))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400)); //@formatter:on

    // Verify that the mock repository is never called
    verifyZeroInteractions(mockExampleRepository);
  }

  @Test
  public void putExample_shouldCallRepositorySaveWithExample()
      throws JsonProcessingException, Exception {

    final long id = 1;

    final Example input = new Example();
    input.setName("Barry Dilon");

    final Example expected = new Example();
    expected.setId(id);
    expected.setName("Barry Dilon");

    when(mockExampleRepository.exists(id)).thenReturn(true);

    // When the mock repository save method is called for an example matching
    // the input
    // then return the expected output
    when(mockExampleRepository.save((Example) argThat(new Equals(expected)))).thenReturn(expected);

    // Perform an HTTP PUT request with the input as JSON
    // and verify that status 200 was received
    // and verify that the expected output was received
    mvc.perform(MockMvcRequestBuilders.put("/examples/" + id) //@formatter:off
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(input))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo(toJson(expected)))); //@formatter:on

    // Verify that mock repository exists was called once
    verify(mockExampleRepository, times(1)).exists(id);

    // Verify that the mock repository save method was called with an example
    // matching the input
    verify(mockExampleRepository, times(1)).save((Example) argThat(new Equals(expected)));
  }

  @Test
  public void putExample_ifContainsIdField_shouldReturn400Error()
      throws JsonProcessingException, Exception {

    final Example input = new Example();
    input.setId(1);

    // Perform an HTTP PUT request with the input as JSON
    // and verify that status 400 was received
    mvc.perform(MockMvcRequestBuilders.put("/examples/" + 1) //@formatter:off
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(input))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400)); //@formatter:on

    // Verify that the mock repository is never called
    verifyZeroInteractions(mockExampleRepository);
  }

  @Test
  public void putExample_ifNoExampleWithGivenId_shouldReturn400Error()
      throws JsonProcessingException, Exception {

    final long id = 1;

    final Example input = new Example();

    when(mockExampleRepository.exists(id)).thenReturn(false);

    // Perform an HTTP PUT request with the input as JSON
    // and verify that status 400 was received
    mvc.perform(MockMvcRequestBuilders.put("/examples/" + id) //@formatter:off
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(input))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400)); //@formatter:on

    // Verify that mock repository exists is called once
    verify(mockExampleRepository, times(1)).exists(id);

    // Verify that mock repository save is never called
    verify(mockExampleRepository, never()).save((Example) argThat(new Equals(input)));
  }

  @Test
  public void deleteExample_shouldCallRepositoryWithDeleteForId() throws Exception {

    final long id = 1;

    when(mockExampleRepository.exists(id)).thenReturn(true);

    // Perform an HTTP DELETE request
    // and verify that status 200 was received
    mvc.perform(MockMvcRequestBuilders.delete("/examples/" + id)).andExpect(status().isOk());

    // Verify that mock repository exists was called once
    verify(mockExampleRepository, times(1)).exists(id);

    // Verify that the mock repository save method was called with an example
    // matching the input
    verify(mockExampleRepository, times(1)).delete(id);
  }

  @Test
  public void deleteExample_ifNoExampleWithGivenId_shouldReturn400Error() throws Exception {

    final long id = 1;

    when(mockExampleRepository.exists(id)).thenReturn(false);

    // Perform an HTTP DELETE request
    // and verify that status 400 was received
    mvc.perform(MockMvcRequestBuilders.delete("/examples/" + id)).andExpect(status().is(400));

    // Verify that mock repository exists is called once
    verify(mockExampleRepository, times(1)).exists(id);

    // Verify that mock repository save is never called
    verify(mockExampleRepository, never()).delete(id);
  }

  private Collection<Example> getTestCollection(final int version) {
    final List<Example> examples = new ArrayList<>();
    if (version == 0) {
      return examples;
    }

    for (int i = 0; i < version; ++i) {
      final Example example = new Example();
      example.setId(i + 1);
      examples.add(example);
    }
    return examples;
  }

  private String toJson(final Example example) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(example);
  }

  private String toJson(final Collection<Example> examples) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(examples);
  }
}
