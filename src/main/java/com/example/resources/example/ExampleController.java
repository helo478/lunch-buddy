package com.example.resources.example;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/examples")
public class ExampleController {

  @Autowired
  private ExampleRepository exampleRepository;

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public Iterable<Example> getExamples() {
    return exampleRepository.findAll();
  }

  @RequestMapping(value = "/{exampleId}", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<Example> getExample(@PathVariable final long exampleId) {
    final Example example = exampleRepository.findOne(exampleId);
    if (example == null) {
      return new ResponseEntity<Example>(HttpStatus.BAD_REQUEST);
    } else {
      return ResponseEntity.ok(example);
    }
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Example> postExample(@Valid @RequestBody final Example example) {
    if (example.getId() != 0) {
      return new ResponseEntity<Example>(HttpStatus.BAD_REQUEST);
    } else {
      return ResponseEntity.ok(exampleRepository.save(example));
    }
  }
  
  @RequestMapping(value = "/{exampleId}", method = RequestMethod.PUT)
  @ResponseBody
  public ResponseEntity<Example> putExample(
      @PathVariable("exampleId") final long exampleId,
      @Valid @RequestBody final Example example) {
    
    if (example.getId() == 0 && exampleRepository.exists(exampleId)) {
      example.setId(exampleId);
      final Example savedExample = exampleRepository.save(example);
      return ResponseEntity.ok(savedExample);
    } else {
      return new ResponseEntity<Example>(HttpStatus.BAD_REQUEST);
    }
  }
  
  @RequestMapping(value = "/{exampleId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteExample(@PathVariable("exampleId") final long exampleId) {
    if (exampleRepository.exists(exampleId)) {
      exampleRepository.delete(exampleId);
      return new ResponseEntity<Void>(HttpStatus.OK);
    } else {
      return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
  }
}
