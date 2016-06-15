package com.example.resources.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends CrudRepository<Example, Long> {

}
