package com.minsait.onesait.microservice.rest;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("user")
@Api(value = "User REST service")
@ApiResponses({ @ApiResponse(code = 429, message = "Too Many Requests"),
		@ApiResponse(code = 500, message = "Error processing request"),
		@ApiResponse(code = 403, message = "Forbidden") })
@Slf4j
public class UserInfo {

	@GetMapping("info")
	@ApiOperation(response = Principal.class, httpMethod = "GET", value = "Return current authenticated user")
	@ApiResponse(code = 429, message = "Too Many Requests")
	@SentinelResource(value = "principalInfo", blockHandler = "handleException")
	public ResponseEntity<Principal> principal(Principal user) {
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	public ResponseEntity<Principal> handleException(Principal user, BlockException ex) {
		log.error("Flow control exception throwed");
		return new ResponseEntity<>(null, HttpStatus.TOO_MANY_REQUESTS);
	}

}
