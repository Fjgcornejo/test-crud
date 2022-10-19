package com.minsait.onesait.microservice.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minsait.onesait.microservice.model.TestCrudWrapper;
import com.minsait.onesait.microservice.repository.TestCrudRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("TestCrud")
@Api(value = "Restaurants REST service", tags = { "TestCrud" })
@ApiResponses({ @ApiResponse(code = 429, message = "Too Many Requests"),
	@ApiResponse(code = 500, message = "Error processing request"),
	@ApiResponse(code = 403, message = "Forbidden") })
@Slf4j
public class TestCrudService {

	@Autowired
	private TestCrudRepository ontologyRepository;

	@GetMapping
	@ApiOperation(response = TestCrudWrapper[].class, httpMethod = "GET", value = "Return all TestCrud")
	@ApiResponse(code = 429, message = "Too Many Requests")
	public ResponseEntity<List<TestCrudWrapper>> getAllTestCruds() {
		log.info("Getting all registered TestCruds");
		final List<TestCrudWrapper> ontologies = ontologyRepository.findAll();

		return new ResponseEntity<>(ontologies, HttpStatus.OK);
	}

	@PostMapping
	@ApiOperation(response = String.class, httpMethod = "POST", value = "Insert one TestCrud instance")
	public ResponseEntity<String> insertTestCrud(@RequestBody TestCrudWrapper testCrudWrapper){
		final String result = ontologyRepository.insertTestCrudEntity(testCrudWrapper);
		return ResponseEntity.ok().body(result);
	}

	@DeleteMapping("/name/{name}")
	@ApiOperation(response = String.class, httpMethod = "DELETE", value = "Delete one TestCrud instance by name")
	public ResponseEntity<String> deleteTestCrudByName(@PathVariable("name") String name){
		final String result = ontologyRepository.deleteByName(name);
		return ResponseEntity.ok().body(result);
	}

}
